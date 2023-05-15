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

import me.zhengjie.modules.store.domain.TsStoreExpress;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsStoreExpressRepository;
import me.zhengjie.modules.store.service.TsStoreExpressService;
import me.zhengjie.modules.store.service.dto.TsStoreExpressDto;
import me.zhengjie.modules.store.service.dto.TsStoreExpressQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsStoreExpressMapper;
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
public class TsStoreExpressServiceImpl implements TsStoreExpressService {

    private final TsStoreExpressRepository tsStoreExpressRepository;
    private final TsStoreExpressMapper tsStoreExpressMapper;

    @Override
    public Map<String,Object> queryAll(TsStoreExpressQueryCriteria criteria, Pageable pageable){
        Page<TsStoreExpress> page = tsStoreExpressRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsStoreExpressMapper::toDto));
    }

    @Override
    public List<TsStoreExpressDto> queryAll(TsStoreExpressQueryCriteria criteria){
        return tsStoreExpressMapper.toDto(tsStoreExpressRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsStoreExpressDto findById(Long id) {
        TsStoreExpress tsStoreExpress = tsStoreExpressRepository.findById(id).orElseGet(TsStoreExpress::new);
        ValidationUtil.isNull(tsStoreExpress.getId(),"TsStoreExpress","id",id);
        return tsStoreExpressMapper.toDto(tsStoreExpress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsStoreExpressDto create(TsStoreExpress resources) {
        resources.setIsDel(false);
        return tsStoreExpressMapper.toDto(tsStoreExpressRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsStoreExpress resources) {
        TsStoreExpress tsStoreExpress = tsStoreExpressRepository.findById(resources.getId()).orElseGet(TsStoreExpress::new);
        ValidationUtil.isNull( tsStoreExpress.getId(),"TsStoreExpress","id",resources.getId());
        tsStoreExpress.copy(resources);
        tsStoreExpressRepository.save(tsStoreExpress);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsStoreExpressRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsStoreExpressDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsStoreExpressDto tsStoreExpress : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建人", tsStoreExpress.getCreateBy());
            map.put("创建时间", tsStoreExpress.getCreateTime());
            map.put("更新人", tsStoreExpress.getUpdateBy());
            map.put("更新时间", tsStoreExpress.getUpdateTime());
            map.put("是否删除;0 失效 1有效", tsStoreExpress.getIsDel());
            map.put("运费", tsStoreExpress.getExpressPrice());
            map.put("满多少免运费", tsStoreExpress.getFreeExpressPrice());
            map.put("店铺id", tsStoreExpress.getStoreId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}