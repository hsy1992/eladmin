package me.zhengjie.modules.api.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
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
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.QueryHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    private final String wxApi = "https://api.weixin.qq.com/sns/jscode2session?appid=wx576896069e68458e&secret=527e969d9d76f86188c0f38b7d3ebac2&js_code=%s&grant_type=authorization_code";

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
        long userId = 0L;
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
        } else  {
            return AppResultBean.ok(list.get(0));
        }
    }
}
