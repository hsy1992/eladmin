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
import me.zhengjie.modules.store.domain.TsStore;
import me.zhengjie.modules.store.service.TsStoreService;
import me.zhengjie.modules.store.service.dto.TsStoreQueryCriteria;
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
@Api(tags = "仓买管理")
@RequestMapping("/api/tsStore")
public class TsStoreController {

    private final TsStoreService tsStoreService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tsStore:list')")
    public void exportTsStore(HttpServletResponse response, TsStoreQueryCriteria criteria) throws IOException {
        tsStoreService.download(tsStoreService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询仓买")
    @ApiOperation("查询仓买")
    @PreAuthorize("@el.check('tsStore:list')")
    public ResponseEntity<Object> queryTsStore(TsStoreQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsStoreService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增仓买")
    @ApiOperation("新增仓买")
    @PreAuthorize("@el.check('tsStore:add')")
    public ResponseEntity<Object> createTsStore(@Validated @RequestBody TsStore resources){
        return new ResponseEntity<>(tsStoreService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改仓买")
    @ApiOperation("修改仓买")
    @PreAuthorize("@el.check('tsStore:edit')")
    public ResponseEntity<Object> updateTsStore(@Validated @RequestBody TsStore resources){
        tsStoreService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除仓买")
    @ApiOperation("删除仓买")
    @PreAuthorize("@el.check('tsStore:del')")
    public ResponseEntity<Object> deleteTsStore(@RequestBody Long[] ids) {
        tsStoreService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}