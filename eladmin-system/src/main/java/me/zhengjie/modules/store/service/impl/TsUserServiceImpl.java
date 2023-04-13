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

import me.zhengjie.modules.store.domain.TsUser;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsUserRepository;
import me.zhengjie.modules.store.service.TsUserService;
import me.zhengjie.modules.store.service.dto.TsUserDto;
import me.zhengjie.modules.store.service.dto.TsUserQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsUserMapper;
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
public class TsUserServiceImpl implements TsUserService {

    private final TsUserRepository tsUserRepository;
    private final TsUserMapper tsUserMapper;

    @Override
    public Map<String,Object> queryAll(TsUserQueryCriteria criteria, Pageable pageable){
        Page<TsUser> page = tsUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tsUserMapper::toDto));
    }

    @Override
    public List<TsUserDto> queryAll(TsUserQueryCriteria criteria){
        return tsUserMapper.toDto(tsUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsUserDto findById(Long id) {
        TsUser tsUser = tsUserRepository.findById(id).orElseGet(TsUser::new);
        ValidationUtil.isNull(tsUser.getId(),"TsUser","id",id);
        return tsUserMapper.toDto(tsUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsUserDto create(TsUser resources) {
        return tsUserMapper.toDto(tsUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsUser resources) {
        TsUser tsUser = tsUserRepository.findById(resources.getId()).orElseGet(TsUser::new);
        ValidationUtil.isNull( tsUser.getId(),"TsUser","id",resources.getId());
        tsUser.copy(resources);
        tsUserRepository.save(tsUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsUserDto tsUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" isDel",  tsUser.getIsDel());
            map.put("用户账户", tsUser.getUsername());
            map.put("用户密码", tsUser.getPassword());
            map.put("真实姓名", tsUser.getRealName());
            map.put("生日", tsUser.getBirthday());
            map.put("身份证号码", tsUser.getIdcard());
            map.put("备注", tsUser.getMark());
            map.put("昵称", tsUser.getNickName());
            map.put("用户头像", tsUser.getAvatar());
            map.put("手机号", tsUser.getPhone());
            map.put("添加时间", tsUser.getCreateTime());
            map.put("最后登录时间", tsUser.getLastLoginTime());
            map.put("详细地址", tsUser.getAddress());
            map.put("用户登录类型", tsUser.getLoginType());
            map.put("微信用户json信息", tsUser.getWxProfile());
            map.put("用户状态1正常0禁止", tsUser.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}