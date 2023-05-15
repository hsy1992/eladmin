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

import me.zhengjie.modules.store.domain.TsUserBill;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsUserBillRepository;
import me.zhengjie.modules.store.service.TsUserBillService;
import me.zhengjie.modules.store.service.dto.TsUserBillDto;
import me.zhengjie.modules.store.service.dto.TsUserBillQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsUserBillMapper;
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
public class TsUserBillServiceImpl implements TsUserBillService {

    private final TsUserBillRepository tsUserBillRepository;
    private final TsUserBillMapper tsUserBillMapper;

    @Override
    public Map<String,Object> queryAll(TsUserBillQueryCriteria criteria, Pageable pageable){
        Page<TsUserBill> page = tsUserBillRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsUserBillMapper::toDto));
    }

    @Override
    public List<TsUserBillDto> queryAll(TsUserBillQueryCriteria criteria){
        return tsUserBillMapper.toDto(tsUserBillRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsUserBillDto findById(Long id) {
        TsUserBill tsUserBill = tsUserBillRepository.findById(id).orElseGet(TsUserBill::new);
        ValidationUtil.isNull(tsUserBill.getId(),"TsUserBill","id",id);
        return tsUserBillMapper.toDto(tsUserBill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsUserBillDto create(TsUserBill resources) {
        resources.setIsDel(false);
        return tsUserBillMapper.toDto(tsUserBillRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsUserBill resources) {
        TsUserBill tsUserBill = tsUserBillRepository.findById(resources.getId()).orElseGet(TsUserBill::new);
        ValidationUtil.isNull( tsUserBill.getId(),"TsUserBill","id",resources.getId());
        tsUserBill.copy(resources);
        tsUserBillRepository.save(tsUserBill);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsUserBillRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsUserBillDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsUserBillDto tsUserBill : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" isDel",  tsUserBill.getIsDel());
            map.put("用户id", tsUserBill.getUid());
            map.put("关联id", tsUserBill.getLinkId());
            map.put("0支出 1获得", tsUserBill.getPm());
            map.put("标题", tsUserBill.getTitle());
            map.put("明细种类", tsUserBill.getCategory());
            map.put("明细类型", tsUserBill.getType());
            map.put("明细数字", tsUserBill.getNumber());
            map.put("备注", tsUserBill.getMark());
            map.put(" createTime",  tsUserBill.getCreateTime());
            map.put(" updateTime",  tsUserBill.getUpdateTime());
            map.put("0 待确认 1有效 -1无效", tsUserBill.getStatus());
            map.put("店铺id", tsUserBill.getStoreId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}