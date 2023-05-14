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
package me.zhengjie.modules.store.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.domain.QiniuContent;
import me.zhengjie.exception.handler.ApiError;
import me.zhengjie.modules.store.domain.TsProductBaseInfo;
import me.zhengjie.modules.store.service.TsProductBaseInfoService;
import me.zhengjie.modules.store.service.dto.TsProductBaseInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 * @author endless
 * @website https://eladmin.vip
 * @date 2023-04-13
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "商品信息管理")
@RequestMapping("/api/tsProductBaseInfo")
public class TsProductBaseInfoController {

    private final TsProductBaseInfoService tsProductBaseInfoService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tsProductBaseInfo:list')")
    public void exportTsProductBaseInfo(HttpServletResponse response, TsProductBaseInfoQueryCriteria criteria) throws IOException {
        tsProductBaseInfoService.download(tsProductBaseInfoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询商品信息")
    @ApiOperation("查询商品信息")
    @PreAuthorize("@el.check('tsProductBaseInfo:list')")
    public ResponseEntity<Object> queryTsProductBaseInfo(TsProductBaseInfoQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(tsProductBaseInfoService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping("/byName")
    @Log("查询商品信息")
    @ApiOperation("查询商品信息")
    @PreAuthorize("@el.check('tsProductBaseInfo:list')")
    public ResponseEntity<Object> queryTsProductBaseInfoByName(@RequestBody TsProductBaseInfoQueryCriteria criteria) {
        return new ResponseEntity<>(tsProductBaseInfoService.queryAllByName(criteria), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增商品信息")
    @ApiOperation("新增商品信息")
    @PreAuthorize("@el.check('tsProductBaseInfo:add')")
    public ResponseEntity<Object> createTsProductBaseInfo(@Validated @RequestBody TsProductBaseInfo resources) {
        if (resources.getSalesPrice() == null) {
            return  new ResponseEntity<>(ApiError.error("请输入售价"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tsProductBaseInfoService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改商品信息")
    @ApiOperation("修改商品信息")
    @PreAuthorize("@el.check('tsProductBaseInfo:edit')")
    public ResponseEntity<Object> updateTsProductBaseInfo(@Validated @RequestBody TsProductBaseInfo resources) {
        tsProductBaseInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除商品信息")
    @ApiOperation("删除商品信息")
    @PreAuthorize("@el.check('tsProductBaseInfo:del')")
    public ResponseEntity<Object> deleteTsProductBaseInfo(@RequestBody Long[] ids) {
        tsProductBaseInfoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}