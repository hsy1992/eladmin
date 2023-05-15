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

import me.zhengjie.modules.store.domain.TsUserCar;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsUserCarRepository;
import me.zhengjie.modules.store.service.TsUserCarService;
import me.zhengjie.modules.store.service.dto.TsUserCarDto;
import me.zhengjie.modules.store.service.dto.TsUserCarQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsUserCarMapper;
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
public class TsUserCarServiceImpl implements TsUserCarService {

    private final TsUserCarRepository tsUserCarRepository;
    private final TsUserCarMapper tsUserCarMapper;

    @Override
    public Map<String,Object> queryAll(TsUserCarQueryCriteria criteria, Pageable pageable){
        Page<TsUserCar> page = tsUserCarRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsUserCarMapper::toDto));
    }

    @Override
    public List<TsUserCarDto> queryAll(TsUserCarQueryCriteria criteria){
        return tsUserCarMapper.toDto(tsUserCarRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsUserCarDto findById(Long id) {
        TsUserCar tsUserCar = tsUserCarRepository.findById(id).orElseGet(TsUserCar::new);
        ValidationUtil.isNull(tsUserCar.getId(),"TsUserCar","id",id);
        return tsUserCarMapper.toDto(tsUserCar);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsUserCarDto create(TsUserCar resources) {
        resources.setIsDel(false);
        return tsUserCarMapper.toDto(tsUserCarRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsUserCar resources) {
        TsUserCar tsUserCar = tsUserCarRepository.findById(resources.getId()).orElseGet(TsUserCar::new);
        ValidationUtil.isNull( tsUserCar.getId(),"TsUserCar","id",resources.getId());
        tsUserCar.copy(resources);
        tsUserCarRepository.save(tsUserCar);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsUserCarRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsUserCarDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsUserCarDto tsUserCar : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建时间", tsUserCar.getCreateTime());
            map.put("更新时间", tsUserCar.getUpdateTime());
            map.put("用户id", tsUserCar.getUid());
            map.put("类型", tsUserCar.getType());
            map.put("是否购买0为购买 1已购买", tsUserCar.getIsPay());
            map.put("是否删除", tsUserCar.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}