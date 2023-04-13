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

import me.zhengjie.modules.store.domain.TsStoreSale;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsStoreSaleRepository;
import me.zhengjie.modules.store.service.TsStoreSaleService;
import me.zhengjie.modules.store.service.dto.TsStoreSaleDto;
import me.zhengjie.modules.store.service.dto.TsStoreSaleQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsStoreSaleMapper;
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
public class TsStoreSaleServiceImpl implements TsStoreSaleService {

    private final TsStoreSaleRepository tsStoreSaleRepository;
    private final TsStoreSaleMapper tsStoreSaleMapper;

    @Override
    public Map<String,Object> queryAll(TsStoreSaleQueryCriteria criteria, Pageable pageable){
        Page<TsStoreSale> page = tsStoreSaleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsStoreSaleMapper::toDto));
    }

    @Override
    public List<TsStoreSaleDto> queryAll(TsStoreSaleQueryCriteria criteria){
        return tsStoreSaleMapper.toDto(tsStoreSaleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsStoreSaleDto findById(Long id) {
        TsStoreSale tsStoreSale = tsStoreSaleRepository.findById(id).orElseGet(TsStoreSale::new);
        ValidationUtil.isNull(tsStoreSale.getId(),"TsStoreSale","id",id);
        return tsStoreSaleMapper.toDto(tsStoreSale);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsStoreSaleDto create(TsStoreSale resources) {
        return tsStoreSaleMapper.toDto(tsStoreSaleRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsStoreSale resources) {
        TsStoreSale tsStoreSale = tsStoreSaleRepository.findById(resources.getId()).orElseGet(TsStoreSale::new);
        ValidationUtil.isNull( tsStoreSale.getId(),"TsStoreSale","id",resources.getId());
        tsStoreSale.copy(resources);
        tsStoreSaleRepository.save(tsStoreSale);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsStoreSaleRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsStoreSaleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsStoreSaleDto tsStoreSale : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建时间", tsStoreSale.getCreateTime());
            map.put(" storeId",  tsStoreSale.getStoreId());
            map.put("年", tsStoreSale.getDate());
            map.put("销量", tsStoreSale.getSales());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}