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

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.druid.sql.visitor.functions.If;
import me.zhengjie.modules.store.domain.TsProductBaseInfo;
import me.zhengjie.tools.ProductCodeUtil;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.store.repository.TsProductBaseInfoRepository;
import me.zhengjie.modules.store.service.TsProductBaseInfoService;
import me.zhengjie.modules.store.service.dto.TsProductBaseInfoDto;
import me.zhengjie.modules.store.service.dto.TsProductBaseInfoQueryCriteria;
import me.zhengjie.modules.store.service.mapstruct.TsProductBaseInfoMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

/**
 * @author endless
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2023-04-13
 **/
@Service
@RequiredArgsConstructor
public class TsProductBaseInfoServiceImpl implements TsProductBaseInfoService {

    private final TsProductBaseInfoRepository tsProductBaseInfoRepository;
    private final TsProductBaseInfoMapper tsProductBaseInfoMapper;

    @Override
    public Map<String, Object> queryAll(TsProductBaseInfoQueryCriteria criteria, Pageable pageable) {
        Page<TsProductBaseInfo> page = tsProductBaseInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(tsProductBaseInfoMapper::toDto));
    }

    @Override
    public List<TsProductBaseInfoDto> queryAll(TsProductBaseInfoQueryCriteria criteria) {
        return tsProductBaseInfoMapper.toDto(tsProductBaseInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public TsProductBaseInfoDto findById(Long id) {
        TsProductBaseInfo tsProductBaseInfo = tsProductBaseInfoRepository.findById(id).orElseGet(TsProductBaseInfo::new);
        ValidationUtil.isNull(tsProductBaseInfo.getId(), "TsProductBaseInfo", "id", id);
        return tsProductBaseInfoMapper.toDto(tsProductBaseInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsProductBaseInfoDto create(TsProductBaseInfo resources) {
        resources.setIsDel(false);
        resources.setCode(ProductCodeUtil.getCode());
        return tsProductBaseInfoMapper.toDto(tsProductBaseInfoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TsProductBaseInfo resources) {
        TsProductBaseInfo tsProductBaseInfo = tsProductBaseInfoRepository.findById(resources.getId()).orElseGet(TsProductBaseInfo::new);
        ValidationUtil.isNull(tsProductBaseInfo.getId(), "TsProductBaseInfo", "id", resources.getId());
        tsProductBaseInfo.copy(resources);
        tsProductBaseInfoRepository.save(tsProductBaseInfo);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            tsProductBaseInfoRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TsProductBaseInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TsProductBaseInfoDto tsProductBaseInfo : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("创建人", tsProductBaseInfo.getCreatedBy());
            map.put("创建时间", tsProductBaseInfo.getCreatedTime());
            map.put("更新人", tsProductBaseInfo.getUpdatedBy());
            map.put("更新时间", tsProductBaseInfo.getUpdatedTime());
            map.put(" isDel", tsProductBaseInfo.getIsDel());
            map.put("商品条码(必填)", tsProductBaseInfo.getCode());
            map.put("商品名称(必填)", tsProductBaseInfo.getName());
            map.put("商品简称(必填)", tsProductBaseInfo.getShortName());
            map.put("规格型号", tsProductBaseInfo.getModel());
            map.put("库存单位(必填)", tsProductBaseInfo.getSize());
            map.put("商品类别名称(必填)", tsProductBaseInfo.getCateName());
            map.put("商品品牌名称(必填)", tsProductBaseInfo.getBrandName());
            map.put("供应商(必填)", tsProductBaseInfo.getSupplier());
            map.put("进货价", tsProductBaseInfo.getBuyPrice());
            map.put("销售价", tsProductBaseInfo.getSalesPrice());
            map.put("计价方式", tsProductBaseInfo.getValuationMethod());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<TsProductBaseInfoDto> queryAllByName(TsProductBaseInfoQueryCriteria criteria) {
        return tsProductBaseInfoMapper.toDto(tsProductBaseInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

}