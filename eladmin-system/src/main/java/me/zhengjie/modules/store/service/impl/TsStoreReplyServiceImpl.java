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

import me.zhengjie.modules.store.domain.TsStoreReply;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsStoreReplyRepository;
import me.zhengjie.modules.store.service.TsStoreReplyService;
import me.zhengjie.modules.store.service.dto.TsStoreReplyDto;
import me.zhengjie.modules.store.service.dto.TsStoreReplyQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsStoreReplyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
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
public class TsStoreReplyServiceImpl implements TsStoreReplyService {

    private final TsStoreReplyRepository tsStoreReplyRepository;
    private final TsStoreReplyMapper tsStoreReplyMapper;

    @Override
    public Map<String,Object> queryAll(TsStoreReplyQueryCriteria criteria, Pageable pageable){
        Page<TsStoreReply> page = tsStoreReplyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsStoreReplyMapper::toDto));
    }

    @Override
    public List<TsStoreReplyDto> queryAll(TsStoreReplyQueryCriteria criteria){
        return tsStoreReplyMapper.toDto(tsStoreReplyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsStoreReplyDto findById(Long id) {
        TsStoreReply tsStoreReply = tsStoreReplyRepository.findById(id).orElseGet(TsStoreReply::new);
        ValidationUtil.isNull(tsStoreReply.getId(),"TsStoreReply","id",id);
        return tsStoreReplyMapper.toDto(tsStoreReply);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsStoreReplyDto create(TsStoreReply resources) {
        return tsStoreReplyMapper.toDto(tsStoreReplyRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsStoreReply resources) {
        TsStoreReply tsStoreReply = tsStoreReplyRepository.findById(resources.getId()).orElseGet(TsStoreReply::new);
        ValidationUtil.isNull( tsStoreReply.getId(),"TsStoreReply","id",resources.getId());
        tsStoreReply.copy(resources);
        tsStoreReplyRepository.save(tsStoreReply);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsStoreReplyRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsStoreReplyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsStoreReplyDto tsStoreReply : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建时间", tsStoreReply.getCreateTime());
            map.put(" storeId",  tsStoreReply.getStoreId());
            map.put("留言内容", tsStoreReply.getContent());
            map.put(" uid",  tsStoreReply.getUid());
            map.put("用户名称", tsStoreReply.getUserName());
            map.put(" isDel",  tsStoreReply.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}