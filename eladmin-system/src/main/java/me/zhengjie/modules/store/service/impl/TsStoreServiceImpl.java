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

import me.zhengjie.modules.security.service.UserCacheManager;
import me.zhengjie.modules.store.domain.TsStore;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsStoreRepository;
import me.zhengjie.modules.store.service.TsStoreService;
import me.zhengjie.modules.store.service.dto.TsStoreDto;
import me.zhengjie.modules.store.service.dto.TsStoreQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsStoreMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author endless
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2023-04-13
 **/
@Service
@RequiredArgsConstructor
public class TsStoreServiceImpl implements TsStoreService {

    private final TsStoreRepository tsStoreRepository;
    private final TsStoreMapper tsStoreMapper;
    private final DeptRepository deptRepository;
    private final UserCacheManager userCacheManager;

    @Override
    public Map<String, Object> queryAll(TsStoreQueryCriteria criteria, Pageable pageable) {
        if (!userCacheManager.getUserCache(SecurityUtils.getCurrentUser().getUsername()).getUser().getIsAdmin()) {
            criteria.setDeptId(userCacheManager.getUserCache(SecurityUtils.getCurrentUser().getUsername()).getUser().getDept().getId());
        }
        Page<TsStore> page = tsStoreRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(tsStoreMapper::toDto));
    }

    @Override
    public List<TsStoreDto> queryAll(TsStoreQueryCriteria criteria) {
        if (!userCacheManager.getUserCache(SecurityUtils.getCurrentUser().getUsername()).getUser().getIsAdmin()) {
            criteria.setDeptId(userCacheManager.getUserCache(SecurityUtils.getCurrentUser().getUsername()).getUser().getDept().getId());
        }
        return tsStoreMapper.toDto(tsStoreRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsStoreDto findById(Long id) {
        TsStore tsStore = tsStoreRepository.findById(id).orElseGet(TsStore::new);
        ValidationUtil.isNull(tsStore.getId(), "TsStore", "id", id);
        return tsStoreMapper.toDto(tsStore);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsStoreDto create(@Validated TsStore resources) {
        resources.setIsDel(false);
        // 先创建对应部门
        Dept dept = new Dept();
        dept.setSubCount(0);
        dept.setName(resources.getName());
        dept.setEnabled(true);
        dept.setDeptSort(0);
        dept = deptRepository.save(dept);
        resources.setDeptId(dept.getId());
        return tsStoreMapper.toDto(tsStoreRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsStore resources) {
        TsStore tsStore = tsStoreRepository.findById(resources.getId()).orElseGet(TsStore::new);
        ValidationUtil.isNull(tsStore.getId(), "TsStore", "id", resources.getId());
        tsStore.copy(resources);
        tsStoreRepository.save(tsStore);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsStoreRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsStoreDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsStoreDto tsStore : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("是否删除;0 删除 1未删除", tsStore.getIsDel());
            map.put("创建人", tsStore.getCreatedBy());
            map.put("创建时间", tsStore.getCreatedTime());
            map.put("更新人", tsStore.getUpdatedBy());
            map.put("更新时间", tsStore.getUpdatedTime());
            map.put("门店Code", tsStore.getCode());
            map.put("门店名称", tsStore.getName());
            map.put("门店电话", tsStore.getPhone());
            map.put("门店地址省市区", tsStore.getAddress());
            map.put("门店详情", tsStore.getAddressDetails());
            map.put("纬度", tsStore.getLatitude());
            map.put("经度", tsStore.getLongitude());
            map.put("门店logo", tsStore.getLogo());
            map.put("门头照片", tsStore.getBanner());
            map.put("门店照片", tsStore.getPics());
            map.put("店铺介绍", tsStore.getIntroduce());
            map.put("营业时间", tsStore.getDayTime());
            map.put("搜索范围km", tsStore.getSearchScope());
            map.put("配送范围", tsStore.getExpressScope());
            map.put("广告", tsStore.getAdvertisement());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}