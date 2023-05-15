/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.store.service.impl;

import me.zhengjie.modules.store.domain.TsStoreOrder;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsStoreOrderRepository;
import me.zhengjie.modules.store.service.TsStoreOrderService;
import me.zhengjie.modules.store.service.dto.TsStoreOrderDto;
import me.zhengjie.modules.store.service.dto.TsStoreOrderQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsStoreOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author endless
* @date 2023-04-13
**/
@Service
@RequiredArgsConstructor
public class TsStoreOrderServiceImpl implements TsStoreOrderService {

    private final TsStoreOrderRepository tsStoreOrderRepository;
    private final TsStoreOrderMapper tsStoreOrderMapper;

    @Override
    public Map<String,Object> queryAll(TsStoreOrderQueryCriteria criteria, Pageable pageable){
        Page<TsStoreOrder> page = tsStoreOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsStoreOrderMapper::toDto));
    }

    @Override
    public List<TsStoreOrderDto> queryAll(TsStoreOrderQueryCriteria criteria){
        return tsStoreOrderMapper.toDto(tsStoreOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsStoreOrderDto findById(Long id) {
        TsStoreOrder tsStoreOrder = tsStoreOrderRepository.findById(id).orElseGet(TsStoreOrder::new);
        ValidationUtil.isNull(tsStoreOrder.getId(),"TsStoreOrder","id",id);
        return tsStoreOrderMapper.toDto(tsStoreOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsStoreOrderDto create(TsStoreOrder resources) {
        resources.setIsDel(false);
        return tsStoreOrderMapper.toDto(tsStoreOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsStoreOrder resources) {
        TsStoreOrder tsStoreOrder = tsStoreOrderRepository.findById(resources.getId()).orElseGet(TsStoreOrder::new);
        ValidationUtil.isNull( tsStoreOrder.getId(),"TsStoreOrder","id",resources.getId());
        tsStoreOrder.copy(resources);
        tsStoreOrderRepository.save(tsStoreOrder);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsStoreOrderRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsStoreOrderDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsStoreOrderDto tsStoreOrder : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建时间", tsStoreOrder.getCreateTime());
            map.put("更新时间", tsStoreOrder.getUpdateTime());
            map.put("订单号", tsStoreOrder.getCode());
            map.put("用户id", tsStoreOrder.getUserId());
            map.put("用户名", tsStoreOrder.getRealName());
            map.put("用户电话", tsStoreOrder.getUserPhone());
            map.put("用户地址", tsStoreOrder.getUserAddress());
            map.put("购物车id", tsStoreOrder.getCartId());
            map.put("运费金额", tsStoreOrder.getExpressPrice());
            map.put("商品总数", tsStoreOrder.getTotalNum());
            map.put("订单总价", tsStoreOrder.getTotalPrice());
            map.put("支付金额", tsStoreOrder.getPayPrice());
            map.put("支付状态", tsStoreOrder.getPaid());
            map.put("支付时间", tsStoreOrder.getPayTime());
            map.put("支付方式", tsStoreOrder.getPayType());
            map.put("-1申请退款 -2 退款成功 0 待发货 1待收货 2 已收货 3已完成", tsStoreOrder.getStatus());
            map.put("备注", tsStoreOrder.getRemark());
            map.put(" isDel",  tsStoreOrder.getIsDel());
            map.put("1配送 2 自提", tsStoreOrder.getShippingType());
            map.put("门店id", tsStoreOrder.getStoreId());
            map.put("唯一id", tsStoreOrder.getUnique());
            map.put("微信订单id", tsStoreOrder.getWxId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}