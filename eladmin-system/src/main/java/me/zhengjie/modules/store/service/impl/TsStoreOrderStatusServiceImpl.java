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

import me.zhengjie.modules.store.domain.TsStoreOrderStatus;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsStoreOrderStatusRepository;
import me.zhengjie.modules.store.service.TsStoreOrderStatusService;
import me.zhengjie.modules.store.service.dto.TsStoreOrderStatusDto;
import me.zhengjie.modules.store.service.dto.TsStoreOrderStatusQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsStoreOrderStatusMapper;
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
public class TsStoreOrderStatusServiceImpl implements TsStoreOrderStatusService {

    private final TsStoreOrderStatusRepository tsStoreOrderStatusRepository;
    private final TsStoreOrderStatusMapper tsStoreOrderStatusMapper;

    @Override
    public Map<String,Object> queryAll(TsStoreOrderStatusQueryCriteria criteria, Pageable pageable){
        Page<TsStoreOrderStatus> page = tsStoreOrderStatusRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsStoreOrderStatusMapper::toDto));
    }

    @Override
    public List<TsStoreOrderStatusDto> queryAll(TsStoreOrderStatusQueryCriteria criteria){
        return tsStoreOrderStatusMapper.toDto(tsStoreOrderStatusRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsStoreOrderStatusDto findById(Long id) {
        TsStoreOrderStatus tsStoreOrderStatus = tsStoreOrderStatusRepository.findById(id).orElseGet(TsStoreOrderStatus::new);
        ValidationUtil.isNull(tsStoreOrderStatus.getId(),"TsStoreOrderStatus","id",id);
        return tsStoreOrderStatusMapper.toDto(tsStoreOrderStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsStoreOrderStatusDto create(TsStoreOrderStatus resources) {
        return tsStoreOrderStatusMapper.toDto(tsStoreOrderStatusRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsStoreOrderStatus resources) {
        TsStoreOrderStatus tsStoreOrderStatus = tsStoreOrderStatusRepository.findById(resources.getId()).orElseGet(TsStoreOrderStatus::new);
        ValidationUtil.isNull( tsStoreOrderStatus.getId(),"TsStoreOrderStatus","id",resources.getId());
        tsStoreOrderStatus.copy(resources);
        tsStoreOrderStatusRepository.save(tsStoreOrderStatus);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsStoreOrderStatusRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsStoreOrderStatusDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsStoreOrderStatusDto tsStoreOrderStatus : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建时间", tsStoreOrderStatus.getChangeTime());
            map.put("操作类型", tsStoreOrderStatus.getChangeType());
            map.put("操作备注", tsStoreOrderStatus.getChangeMessage());
            map.put("订单id", tsStoreOrderStatus.getOrderId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}