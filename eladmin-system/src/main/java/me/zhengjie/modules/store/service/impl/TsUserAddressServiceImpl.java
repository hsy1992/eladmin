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

import me.zhengjie.modules.store.domain.TsUserAddress;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsUserAddressRepository;
import me.zhengjie.modules.store.service.TsUserAddressService;
import me.zhengjie.modules.store.service.dto.TsUserAddressDto;
import me.zhengjie.modules.store.service.dto.TsUserAddressQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsUserAddressMapper;
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
public class TsUserAddressServiceImpl implements TsUserAddressService {

    private final TsUserAddressRepository tsUserAddressRepository;
    private final TsUserAddressMapper tsUserAddressMapper;

    @Override
    public Map<String,Object> queryAll(TsUserAddressQueryCriteria criteria, Pageable pageable){
        Page<TsUserAddress> page = tsUserAddressRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsUserAddressMapper::toDto));
    }

    @Override
    public List<TsUserAddressDto> queryAll(TsUserAddressQueryCriteria criteria){
        return tsUserAddressMapper.toDto(tsUserAddressRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsUserAddressDto findById(Long id) {
        TsUserAddress tsUserAddress = tsUserAddressRepository.findById(id).orElseGet(TsUserAddress::new);
        ValidationUtil.isNull(tsUserAddress.getId(),"TsUserAddress","id",id);
        return tsUserAddressMapper.toDto(tsUserAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsUserAddressDto create(TsUserAddress resources) {
        return tsUserAddressMapper.toDto(tsUserAddressRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsUserAddress resources) {
        TsUserAddress tsUserAddress = tsUserAddressRepository.findById(resources.getId()).orElseGet(TsUserAddress::new);
        ValidationUtil.isNull( tsUserAddress.getId(),"TsUserAddress","id",resources.getId());
        tsUserAddress.copy(resources);
        tsUserAddressRepository.save(tsUserAddress);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsUserAddressRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsUserAddressDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsUserAddressDto tsUserAddress : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" isDel",  tsUserAddress.getIsDel());
            map.put("用户id", tsUserAddress.getUid());
            map.put("收货人", tsUserAddress.getRealName());
            map.put("电话", tsUserAddress.getPhone());
            map.put("省", tsUserAddress.getProvince());
            map.put("市", tsUserAddress.getCity());
            map.put("区", tsUserAddress.getDistrict());
            map.put("详细地址", tsUserAddress.getDetails());
            map.put("邮编", tsUserAddress.getPostCode());
            map.put("是否默认", tsUserAddress.getIsDefault());
            map.put("创建时间", tsUserAddress.getCreateTime());
            map.put("更新时间", tsUserAddress.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}