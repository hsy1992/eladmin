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
import me.zhengjie.modules.store.domain.TsStoreExpress;
import me.zhengjie.modules.store.service.TsStoreExpressService;
import me.zhengjie.modules.store.service.dto.TsStoreExpressQueryCriteria;
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
@Api(tags = "运费配置管理")
@RequestMapping("/api/tsStoreExpress")
public class TsStoreExpressController {

    private final TsStoreExpressService tsStoreExpressService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void exportTsStoreExpress(HttpServletResponse response, TsStoreExpressQueryCriteria criteria) throws IOException {
        tsStoreExpressService.download(tsStoreExpressService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询运费配置")
    @ApiOperation("查询运费配置")
    public ResponseEntity<Object> queryTsStoreExpress(TsStoreExpressQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsStoreExpressService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增运费配置")
    @ApiOperation("新增运费配置")
    public ResponseEntity<Object> createTsStoreExpress(@Validated @RequestBody TsStoreExpress resources){
        return new ResponseEntity<>(tsStoreExpressService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改运费配置")
    @ApiOperation("修改运费配置")
    public ResponseEntity<Object> updateTsStoreExpress(@Validated @RequestBody TsStoreExpress resources){
        tsStoreExpressService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除运费配置")
    @ApiOperation("删除运费配置")
    public ResponseEntity<Object> deleteTsStoreExpress(@RequestBody Long[] ids) {
        tsStoreExpressService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}