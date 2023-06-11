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
import me.zhengjie.modules.store.domain.TsProductClassify;
import me.zhengjie.modules.store.service.TsProductClassifyService;
import me.zhengjie.modules.store.service.dto.TsProductClassifyQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author endless
* @date 2023-04-13
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "分类配置管理")
@RequestMapping("/api/tsProductClassify")
public class TsProductClassifyController {

    private final TsProductClassifyService tsProductClassifyService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void exportTsProductClassify(HttpServletResponse response, TsProductClassifyQueryCriteria criteria) throws IOException {
        tsProductClassifyService.download(tsProductClassifyService.queryAll(criteria), response);
    }

    @Log("查询全部分类")
    @ApiOperation("查询全部分类")
    @GetMapping(value = "/list")
    public ResponseEntity<Object> getProductClassifyList(TsProductClassifyQueryCriteria criteria) {
        return new ResponseEntity<>(tsProductClassifyService.queryAll(criteria), HttpStatus.OK);
    }

    @GetMapping
    @Log("查询分类配置")
    @ApiOperation("查询分类配置")
    public ResponseEntity<Object> queryTsProductClassify(TsProductClassifyQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsProductClassifyService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增分类配置")
    @ApiOperation("新增分类配置")
    public ResponseEntity<Object> createTsProductClassify(@Validated @RequestBody TsProductClassify resources){
        return new ResponseEntity<>(tsProductClassifyService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改分类配置")
    @ApiOperation("修改分类配置")
    public ResponseEntity<Object> updateTsProductClassify(@Validated @RequestBody TsProductClassify resources){
        tsProductClassifyService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除分类配置")
    @ApiOperation("删除分类配置")
    public ResponseEntity<Object> deleteTsProductClassify(@RequestBody Long[] ids) {
        tsProductClassifyService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}