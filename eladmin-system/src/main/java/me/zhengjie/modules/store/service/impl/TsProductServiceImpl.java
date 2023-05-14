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

import me.zhengjie.modules.security.config.bean.LoginProperties;
import me.zhengjie.modules.security.service.UserCacheManager;
import me.zhengjie.modules.store.domain.TsProduct;
import me.zhengjie.tools.ProductCodeUtil;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsProductRepository;
import me.zhengjie.modules.store.service.TsProductService;
import me.zhengjie.modules.store.service.dto.TsProductDto;
import me.zhengjie.modules.store.service.dto.TsProductQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Function;

/**
 * @author endless
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2023-04-13
 **/
@Service
@RequiredArgsConstructor
public class TsProductServiceImpl implements TsProductService {

    private final TsProductRepository tsProductRepository;
    private final TsProductMapper tsProductMapper;
    private final UserCacheManager userCacheManager;
    private static final Logger log = LoggerFactory.getLogger(TsProductServiceImpl.class);

    @Override
    public Map<String, Object> queryAll(TsProductQueryCriteria criteria, Pageable pageable) {
        criteria.setDeptId(userCacheManager.getUserCache(SecurityUtils.getCurrentUser().getUsername()).getUser().getDept().getId());
        Page<TsProduct> page = tsProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(tsProductMapper::toDto));
    }

    @Override
    public List<TsProductDto> queryAll(TsProductQueryCriteria criteria) {
        criteria.setDeptId(userCacheManager.getUserCache("").getUser().getDeptId());
        return tsProductMapper.toDto(tsProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsProductDto findById(Long id) {
        TsProduct tsProduct = tsProductRepository.findById(id).orElseGet(TsProduct::new);
        ValidationUtil.isNull(tsProduct.getId(), "TsProduct", "id", id);
        return tsProductMapper.toDto(tsProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsProductDto create(TsProduct resources) {
        resources.setStoreId(userCacheManager.getUserCache(SecurityUtils.getCurrentUser().getUsername()).getUser().getDeptId());
        resources.setSales(0);
        resources.setBarCode(ProductCodeUtil.getCode());
        return tsProductMapper.toDto(tsProductRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsProduct resources) {
        TsProduct tsProduct = tsProductRepository.findById(resources.getId()).orElseGet(TsProduct::new);
        ValidationUtil.isNull(tsProduct.getId(), "TsProduct", "id", resources.getId());
        tsProduct.copy(resources);
        tsProductRepository.save(tsProduct);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsProductRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsProductDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsProductDto tsProduct : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("创建人", tsProduct.getCreatedBy());
            map.put("创建时间", tsProduct.getCreatedTime());
            map.put("更新人", tsProduct.getUpdatedBy());
            map.put("更新时间", tsProduct.getUpdatedTime());
            map.put(" isDel", tsProduct.getIsDel());
            map.put("商品图片", tsProduct.getImage());
            map.put("商品名称", tsProduct.getName());
            map.put("商品简介", tsProduct.getInfo());
            map.put("条码", tsProduct.getBarCode());
            map.put("关键字", tsProduct.getKeyword());
            map.put("分类id", tsProduct.getCateId());
            map.put("价格", tsProduct.getPrice());
            map.put("排序", tsProduct.getSort());
            map.put("是否展示", tsProduct.getIsShow());
            map.put("产品描述", tsProduct.getDescription());
            map.put("虚拟销量", tsProduct.getFicti());
            map.put("销量", tsProduct.getSales());
            map.put("店铺id", tsProduct.getStoreId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}