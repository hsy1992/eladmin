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

import me.zhengjie.modules.store.domain.TsUserOrderCarInfo;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsUserOrderCarInfoRepository;
import me.zhengjie.modules.store.service.TsUserOrderCarInfoService;
import me.zhengjie.modules.store.service.dto.TsUserOrderCarInfoDto;
import me.zhengjie.modules.store.service.dto.TsUserOrderCarInfoQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsUserOrderCarInfoMapper;
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
public class TsUserOrderCarInfoServiceImpl implements TsUserOrderCarInfoService {

    private final TsUserOrderCarInfoRepository tsUserOrderCarInfoRepository;
    private final TsUserOrderCarInfoMapper tsUserOrderCarInfoMapper;

    @Override
    public Map<String,Object> queryAll(TsUserOrderCarInfoQueryCriteria criteria, Pageable pageable){
        Page<TsUserOrderCarInfo> page = tsUserOrderCarInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsUserOrderCarInfoMapper::toDto));
    }

    @Override
    public List<TsUserOrderCarInfoDto> queryAll(TsUserOrderCarInfoQueryCriteria criteria){
        return tsUserOrderCarInfoMapper.toDto(tsUserOrderCarInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsUserOrderCarInfoDto findById(Long id) {
        TsUserOrderCarInfo tsUserOrderCarInfo = tsUserOrderCarInfoRepository.findById(id).orElseGet(TsUserOrderCarInfo::new);
        ValidationUtil.isNull(tsUserOrderCarInfo.getId(),"TsUserOrderCarInfo","id",id);
        return tsUserOrderCarInfoMapper.toDto(tsUserOrderCarInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsUserOrderCarInfoDto create(TsUserOrderCarInfo resources) {
        return tsUserOrderCarInfoMapper.toDto(tsUserOrderCarInfoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsUserOrderCarInfo resources) {
        TsUserOrderCarInfo tsUserOrderCarInfo = tsUserOrderCarInfoRepository.findById(resources.getId()).orElseGet(TsUserOrderCarInfo::new);
        ValidationUtil.isNull( tsUserOrderCarInfo.getId(),"TsUserOrderCarInfo","id",resources.getId());
        tsUserOrderCarInfo.copy(resources);
        tsUserOrderCarInfoRepository.save(tsUserOrderCarInfo);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsUserOrderCarInfoRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsUserOrderCarInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsUserOrderCarInfoDto tsUserOrderCarInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建时间", tsUserOrderCarInfo.getCreateTime());
            map.put("更新时间", tsUserOrderCarInfo.getUpdateTime());
            map.put("订单id", tsUserOrderCarInfo.getOrderId());
            map.put("购物车id", tsUserOrderCarInfo.getCartId());
            map.put("商品id", tsUserOrderCarInfo.getProductId());
            map.put("购买商品时的快照信息", tsUserOrderCarInfo.getCartInfo());
            map.put("商品数量", tsUserOrderCarInfo.getProductNum());
            map.put(" isDel",  tsUserOrderCarInfo.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}