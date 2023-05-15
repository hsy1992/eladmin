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

import me.zhengjie.modules.store.domain.TsProductClassify;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsProductClassifyRepository;
import me.zhengjie.modules.store.service.TsProductClassifyService;
import me.zhengjie.modules.store.service.dto.TsProductClassifyDto;
import me.zhengjie.modules.store.service.dto.TsProductClassifyQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsProductClassifyMapper;
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
public class TsProductClassifyServiceImpl implements TsProductClassifyService {

    private final TsProductClassifyRepository tsProductClassifyRepository;
    private final TsProductClassifyMapper tsProductClassifyMapper;

    @Override
    public Map<String,Object> queryAll(TsProductClassifyQueryCriteria criteria, Pageable pageable){
        Page<TsProductClassify> page = tsProductClassifyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsProductClassifyMapper::toDto));
    }

    @Override
    public List<TsProductClassifyDto> queryAll(TsProductClassifyQueryCriteria criteria){
        return tsProductClassifyMapper.toDto(tsProductClassifyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsProductClassifyDto findById(Long id) {
        TsProductClassify tsProductClassify = tsProductClassifyRepository.findById(id).orElseGet(TsProductClassify::new);
        ValidationUtil.isNull(tsProductClassify.getId(),"TsProductClassify","id",id);
        return tsProductClassifyMapper.toDto(tsProductClassify);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsProductClassifyDto create(TsProductClassify resources) {
        resources.setIsDel(false);
        return tsProductClassifyMapper.toDto(tsProductClassifyRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsProductClassify resources) {
        TsProductClassify tsProductClassify = tsProductClassifyRepository.findById(resources.getId()).orElseGet(TsProductClassify::new);
        ValidationUtil.isNull( tsProductClassify.getId(),"TsProductClassify","id",resources.getId());
        tsProductClassify.copy(resources);
        tsProductClassifyRepository.save(tsProductClassify);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsProductClassifyRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsProductClassifyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsProductClassifyDto tsProductClassify : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" isDel",  tsProductClassify.getIsDel());
            map.put("创建时间", tsProductClassify.getCreatedTime());
            map.put("更新时间", tsProductClassify.getUpdatedTime());
            map.put("父id", tsProductClassify.getPid());
            map.put("分类名称", tsProductClassify.getCateName());
            map.put("排序", tsProductClassify.getSort());
            map.put("图标", tsProductClassify.getIcon());
            map.put("是否推荐", tsProductClassify.getIsShow());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}