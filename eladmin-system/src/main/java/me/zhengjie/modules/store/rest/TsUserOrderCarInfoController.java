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
import me.zhengjie.modules.store.domain.TsUserOrderCarInfo;
import me.zhengjie.modules.store.service.TsUserOrderCarInfoService;
import me.zhengjie.modules.store.service.dto.TsUserOrderCarInfoQueryCriteria;
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
@Api(tags = "购物车管理")
@RequestMapping("/api/tsUserOrderCarInfo")
public class TsUserOrderCarInfoController {

    private final TsUserOrderCarInfoService tsUserOrderCarInfoService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tsUserOrderCarInfo:list')")
    public void exportTsUserOrderCarInfo(HttpServletResponse response, TsUserOrderCarInfoQueryCriteria criteria) throws IOException {
        tsUserOrderCarInfoService.download(tsUserOrderCarInfoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询购物车")
    @ApiOperation("查询购物车")
    @PreAuthorize("@el.check('tsUserOrderCarInfo:list')")
    public ResponseEntity<Object> queryTsUserOrderCarInfo(TsUserOrderCarInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsUserOrderCarInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增购物车")
    @ApiOperation("新增购物车")
    @PreAuthorize("@el.check('tsUserOrderCarInfo:add')")
    public ResponseEntity<Object> createTsUserOrderCarInfo(@Validated @RequestBody TsUserOrderCarInfo resources){
        return new ResponseEntity<>(tsUserOrderCarInfoService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改购物车")
    @ApiOperation("修改购物车")
    @PreAuthorize("@el.check('tsUserOrderCarInfo:edit')")
    public ResponseEntity<Object> updateTsUserOrderCarInfo(@Validated @RequestBody TsUserOrderCarInfo resources){
        tsUserOrderCarInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除购物车")
    @ApiOperation("删除购物车")
    @PreAuthorize("@el.check('tsUserOrderCarInfo:del')")
    public ResponseEntity<Object> deleteTsUserOrderCarInfo(@RequestBody Long[] ids) {
        tsUserOrderCarInfoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}