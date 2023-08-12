package me.zhengjie.modules.api.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.api.domain.*;
import me.zhengjie.modules.api.service.AppApiService;
import me.zhengjie.modules.api.service.dto.AppProductQueryCriteria;
import me.zhengjie.modules.api.service.dto.HomeStoreDTO;
import me.zhengjie.modules.api.service.dto.ProductListByStoreRequest;
import me.zhengjie.modules.store.domain.*;
import me.zhengjie.modules.store.repository.*;
import me.zhengjie.modules.store.service.TsUserService;
import me.zhengjie.modules.store.service.dto.TsStoreExpressDto;
import me.zhengjie.modules.store.service.dto.TsUserAddressDto;
import me.zhengjie.modules.store.service.mapstruct.TsProductBaseInfoMapper;
import me.zhengjie.modules.store.service.mapstruct.TsUserAddressMapper;
import me.zhengjie.tools.GpsUtil;
import me.zhengjie.tools.PayTool;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.QueryHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class AppApiServiceImpl implements AppApiService {
    private static final Logger log = LoggerFactory.getLogger(AppApiServiceImpl.class);

    private final TsUserService tsUserService;
    private final TsUserRepository tsUserRepository;
    private final TsStoreRepository tsStoreRepository;
    private final TsProductClassifyRepository tsProductClassifyRepository;
    private final TsProductRepository tsProductRepository;
    private final TsStoreExpressRepository tsStoreExpressRepository;
    private final TsUserAddressRepository tsUserAddressRepository;
    private final TsUserAddressMapper tsUserAddressMapper;
    private final TsStoreOrderRepository tsStoreOrderRepository;
    private final TsUserOrderCarInfoRepository tsUserOrderCarInfoRepository;
    private final String wxApi = "https://api.weixin.qq.com/sns/jscode2session?appid=wx1b9c92940ccdc573&secret=cd25eff0ff59040ec58b7554855410df&js_code=%s&grant_type=authorization_code";

    @Override
    public AppResultBean login(TsUser tsUser) {
        //发送delete请求并接收响应数据
        String result = HttpUtil.get(String.format(wxApi, tsUser.getUsername()));
        Map<String, String> map = JSON.parseObject(result, Map.class);
        String username = map.get("openid");
        tsUser.setUsername(username);
        log.info("username:" + username);
        AppUserQueryCriteria criteria = new AppUserQueryCriteria();
        criteria.setUsername(tsUser.getUsername());
        Optional<TsUser> appUser = tsUserRepository.findOne((Specification<TsUser>) (root, query, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        long userId;
        if (!appUser.isPresent()) {
            log.info("创建用户:" + tsUser);
            userId = tsUserService.create(tsUser).getId();
        } else {
            userId = appUser.get().getId();
        }
        return AppResultBean.ok(userId, "登录成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppResultBean<HomeStoreDTO> queryHomeStoreList(String lat, String lng) {
        HomeStoreDTO homeStoreDTO = new HomeStoreDTO();
        List<StoreInfoDTO> nearList = new ArrayList<>();
        List<StoreInfoDTO> recommendList = tsStoreRepository.findAll()
                .stream()
                .map(tsStore -> {
                    double distance = GpsUtil.Distance(Double.parseDouble(lat), Double.parseDouble(lng), Double.parseDouble(tsStore.getLatitude()),
                            Double.parseDouble(tsStore.getLongitude())) / 1000;
                    distance = NumberUtil.round(distance, 2).doubleValue();
                    log.info("distance======" + distance);
                    return StoreInfoDTO.builder()
                            .id(tsStore.getDeptId())
                            .name(tsStore.getName())
                            .addressDetails(tsStore.getAddressDetails())
                            .dayTime(tsStore.getDayTime())
                            .logo(tsStore.getLogo())
                            .phone(tsStore.getPhone())
                            .introduce(tsStore.getIntroduce())
                            .advertisement(tsStore.getAdvertisement())
                            .distance(distance)
                            .searchScope(tsStore.getSearchScope())
                            .build();
                })
                .filter(tsStore -> {
                    boolean hasNear = tsStore.getDistance() < tsStore.getSearchScope();
                    if (hasNear) {
                        nearList.add(tsStore);
                    }
                    return true;
                })
                .limit(3)
                .collect(Collectors.toList());
        homeStoreDTO.setNearList(nearList);
        homeStoreDTO.setRecommendList(recommendList);
        return AppResultBean.ok(homeStoreDTO, "登录成功");
    }

    /**
     * 查询分类
     *
     * @param request
     * @return
     */
    @Override
    public AppResultBean<List<AppStoreProductDTO>> queryCateByStore(ProductListByStoreRequest request) {
        AppProductQueryCriteria appProductQueryCriteria = new AppProductQueryCriteria();
        appProductQueryCriteria.setStoreId(request.getDeptId());
        Map<String, List<TsProduct>> map = new HashMap<>();
        tsProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, appProductQueryCriteria, criteriaBuilder))
                .forEach(tsProduct -> map.computeIfAbsent(tsProduct.getCateName(), k -> new ArrayList<>()).add(tsProduct));
        List<AppStoreProductDTO> list = new ArrayList<>();
        map.forEach((s, tsProducts) -> {
            AppStoreProductDTO appStoreProductDTO = new AppStoreProductDTO();
            appStoreProductDTO.setCateName(s);
            appStoreProductDTO.setList(tsProducts);
            list.add(appStoreProductDTO);
        });
        return AppResultBean.ok(list);
    }

    @Override
    public AppResultBean<TsStoreExpress> queryExpress(ProductListByStoreRequest request) {
        AppProductQueryCriteria appProductQueryCriteria = new AppProductQueryCriteria();
        appProductQueryCriteria.setStoreId(request.getDeptId());
        Optional<TsStoreExpress> dto = tsStoreExpressRepository.findOne((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, appProductQueryCriteria, criteriaBuilder));
        return AppResultBean.ok(dto.orElse(null));
    }

    /**
     * 查询用户地址
     *
     * @param request
     * @return
     */
    @Override
    public AppResultBean<List<TsUserAddressDto>> queryAddressByUser(AppUserIdQuery request) {
        List<TsUserAddress> list = tsUserAddressRepository.findAll((Specification<TsUserAddress>) (root, query, criteriaBuilder) -> QueryHelp.getPredicate(root, request, criteriaBuilder));
        return AppResultBean.ok(tsUserAddressMapper.toDto(list));
    }

    @Override
    public AppResultBean<TsUserAddressDto> queryAddressById(AppAddressIdQuery request) {
        Optional<TsUserAddress> data = tsUserAddressRepository.findOne((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, request, criteriaBuilder));
        return AppResultBean.ok(tsUserAddressMapper.toDto(data.orElse(null)));
    }

    @Override
    public AppResultBean<Object> saveAddress(TsUserAddressDto request) {
        request.setIsDefault(false);
        request.setIsDel(false);
        tsUserAddressRepository.save(tsUserAddressMapper.toEntity(request));
        return AppResultBean.ok(null);
    }

    @Override
    public AppResultBean<Object> updateAddress(TsUserAddressDto request) {
        tsUserAddressRepository.saveAndFlush(tsUserAddressMapper.toEntity(request));
        return AppResultBean.ok(null);
    }

    @Override
    public AppResultBean<Object> setDefaultAddress(AppAddressIdQuery request) {
        tsUserAddressRepository.setDefaultAddress(request.getUid());
        tsUserAddressRepository.setDefaultAddressTrue(request.getId());
        return AppResultBean.ok(null);
    }

    @Override
    public AppResultBean<TsUserAddress> queryDefaultAddress(AppUserIdQuery request) {
        List<TsUserAddress> list = tsUserAddressRepository.findAll((Specification<TsUserAddress>) (root, query, criteriaBuilder) -> QueryHelp.getPredicate(root, request, criteriaBuilder))
                .stream()
                .sorted(Comparator.comparing(TsUserAddress::getIsDefault).reversed())
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            return AppResultBean.ok(null);
        } else {
            return AppResultBean.ok(list.get(0));
        }
    }

    /**
     * 提交订单
     *
     * @param request
     * @return
     */
    @Override
    public AppResultBean<Object> submitOrder(AppOrderDto request) {
        if (request.getCarList().isEmpty()) {
            return AppResultBean.fail(null, "购物车商品为空");
        }
        TsStoreOrder tsStoreOrder = new TsStoreOrder();
        tsStoreOrder.setUserId(Long.parseLong(request.getUid()));
        tsStoreOrder.setStoreId(request.getCarList().get(0).getStoreId());
        tsStoreOrder.setRemark(request.getRemarks());
        tsStoreOrder.setRealName(request.getUserAddrName());
        tsStoreOrder.setUserPhone(request.getUserAddrPhone());
        tsStoreOrder.setUserAddress(request.getUserAddrInfo());
        tsStoreOrder.setExpressPrice(new BigDecimal(request.getExpressPrice()));
        tsStoreOrder.setPayPrice(new BigDecimal(request.getTotalPrice()));
        tsStoreOrder.setTotalPrice(new BigDecimal(request.getTotalPrice()));
        tsStoreOrder.setPayType("微信");
        tsStoreOrder.setIsDel(Boolean.FALSE);
        tsStoreOrder.setStatus(0);
        tsStoreOrder.setWxId(UUID.fastUUID().toString(true));
        tsStoreOrder.setTotalNum(request.getCarList().size());
        tsStoreOrder.setCreateTime(DateUtil.date().toTimestamp());
        TsStoreOrder result = tsStoreOrderRepository.save(tsStoreOrder);
        // 加入购物车id
        List<TsUserOrderCarInfo> carList = new ArrayList<>();
        request.getCarList().forEach(appOrderProductDto -> {
            TsUserOrderCarInfo tsUserOrderCarInfo = new TsUserOrderCarInfo();
            tsUserOrderCarInfo.setOrderId(result.getId());
            tsUserOrderCarInfo.setIsDel(Boolean.FALSE);
            tsUserOrderCarInfo.setProductId(Long.parseLong(appOrderProductDto.getId()));
            tsUserOrderCarInfo.setProductNum(appOrderProductDto.getNum());
            tsUserOrderCarInfo.setCreateTime(DateUtil.date().toTimestamp());
            tsUserOrderCarInfo.setName(appOrderProductDto.getName());
            tsUserOrderCarInfo.setProductPrice(new BigDecimal(appOrderProductDto.getPrice()));
            carList.add(tsUserOrderCarInfo);
        });
        tsUserOrderCarInfoRepository.saveAll(carList);
        Map<String, String> response = new HashMap<>();
        response.put("orderId", result.getId().toString());
        return AppResultBean.ok(response);
    }

    /**
     * 订单列表数据
     *
     * @param request
     * @return
     */
    @Override
    public AppResultBean<List<AppOrderResponse>> orderList(AppOrderListRequest request) {
        List<TsStoreOrder> orderList = tsStoreOrderRepository.findAll((Specification<TsStoreOrder>) (root, query, criteriaBuilder) -> QueryHelp.getPredicate(root, request, criteriaBuilder), Sort.by(Sort.Direction.DESC, "createTime"));
        List<Long> ids = orderList.stream().map(TsStoreOrder::getId).collect(Collectors.toList());
        AppCarInfoQueryCriteria appCarInfoQueryCriteria = new AppCarInfoQueryCriteria();
        appCarInfoQueryCriteria.setOrderId(ids);
        List<TsUserOrderCarInfo> carInfoList = tsUserOrderCarInfoRepository.findAll((Specification<TsUserOrderCarInfo>) (root, query, criteriaBuilder) -> QueryHelp.getPredicate(root, appCarInfoQueryCriteria, criteriaBuilder));
        List<AppOrderResponse> orderResponseList = new ArrayList<>();
        orderList.forEach(tsStoreOrder -> {
            AppOrderResponse appOrderResponse = buildOrder(tsStoreOrder);
            appOrderResponse.setCarList(carInfoList.stream().filter(tsUserOrderCarInfo -> Objects.equals(tsUserOrderCarInfo.getOrderId(), tsStoreOrder.getId())).map(tsUserOrderCarInfo -> {
                AppOrderProductDto appOrderProductDto = new AppOrderProductDto();
                appOrderProductDto.setId(tsUserOrderCarInfo.getId().toString());
                appOrderProductDto.setName(tsUserOrderCarInfo.getName());
                appOrderProductDto.setNum(tsUserOrderCarInfo.getProductNum());
                appOrderProductDto.setPrice(tsUserOrderCarInfo.getProductPrice().toString());
                return appOrderProductDto;
            }).collect(Collectors.toList()));
            orderResponseList.add(appOrderResponse);
        });

        return AppResultBean.ok(orderResponseList);
    }

    /**
     * 订单详情
     *
     * @param request
     * @return
     */
    @Override
    public AppResultBean<AppOrderResponse> orderDetails(AppOrderListRequest request) {
        TsStoreOrder tsStoreOrder = tsStoreOrderRepository.findOne((Specification<TsStoreOrder>) (root, query, criteriaBuilder) -> QueryHelp.getPredicate(root, request, criteriaBuilder)).get();
        List<TsUserOrderCarInfo> carInfoList = tsUserOrderCarInfoRepository.findAllByOrderId(tsStoreOrder.getId());
        TsStore tsStore = tsStoreRepository.findByDeptId(tsStoreOrder.getStoreId());
        AppOrderResponse appOrderResponse = buildOrder(tsStoreOrder);
        appOrderResponse.setShopName(tsStore.getName());
        appOrderResponse.setCarList(carInfoList.stream().filter(tsUserOrderCarInfo -> Objects.equals(tsUserOrderCarInfo.getOrderId(), tsStoreOrder.getId())).map(tsUserOrderCarInfo -> {
            AppOrderProductDto appOrderProductDto = new AppOrderProductDto();
            appOrderProductDto.setId(tsUserOrderCarInfo.getId().toString());
            appOrderProductDto.setName(tsUserOrderCarInfo.getName());
            appOrderProductDto.setNum(tsUserOrderCarInfo.getProductNum());
            appOrderProductDto.setPrice(tsUserOrderCarInfo.getProductPrice().toString());
            return appOrderProductDto;
        }).collect(Collectors.toList()));
        return AppResultBean.ok(appOrderResponse);
    }

    /**
     * 修改订单状态
     *
     * @param request
     * @return
     */
    @Override
    public AppResultBean<Object> orderEditStatus(AppOrderListRequest request) {
        tsStoreOrderRepository.updateOrderStatus(request.getId(), request.getStatus());
        return AppResultBean.ok(null);
    }

    /**
     * 订单支付
     * @param request
     * @return
     */
    @Override
    public AppResultBean<Object> orderPay(AppOrderListRequest request) {
        AppOrderResponse tsStoreOrder = orderDetails(request).getData();
        if (tsStoreOrder == null) {
            return AppResultBean.fail(null, "订单未找到");
        }
        PrepayWithRequestPaymentResponse response = PayTool.prepayWithRequestPayment(tsStoreOrder, wxId -> {
            tsStoreOrderRepository.updateWxId(Long.parseLong(tsStoreOrder.getOrderId()),wxId);
        });
        return AppResultBean.ok(response);
    }

    @Override
    public AppResultBean<Object> payResult(RequestParam request) {
        log.info(request.toString());
        Transaction result = PayTool.getInstance().parse(request);
        if (result.getTradeState() == Transaction.TradeStateEnum.SUCCESS) {
            //支付成功
            tsStoreOrderRepository.updateStatusByWxId(1, result.getOutTradeNo());
        }
        return AppResultBean.ok("");
    }

    private AppOrderResponse buildOrder(TsStoreOrder tsStoreOrder) {
        AppOrderResponse appOrderResponse = new AppOrderResponse();
        appOrderResponse.setRemarks(tsStoreOrder.getRemark());
        appOrderResponse.setUserAddrName(tsStoreOrder.getRealName());
        appOrderResponse.setUserAddrPhone(tsStoreOrder.getUserPhone());
        appOrderResponse.setUserAddrInfo(tsStoreOrder.getUserAddress());
        appOrderResponse.setExpressPrice(tsStoreOrder.getExpressPrice().toString());
        appOrderResponse.setTotalPrice(tsStoreOrder.getTotalPrice().toString());
        appOrderResponse.setStatus(tsStoreOrder.getStatus());
        appOrderResponse.setOrderId(tsStoreOrder.getId().toString());
        appOrderResponse.setCreateTime(tsStoreOrder.getCreateTime().toString());
        appOrderResponse.setWxId(tsStoreOrder.getWxId());
        return appOrderResponse;
    }
}
