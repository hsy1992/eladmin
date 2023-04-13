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
import me.zhengjie.modules.store.domain.TsStoreReply;
import me.zhengjie.modules.store.service.TsStoreReplyService;
import me.zhengjie.modules.store.service.dto.TsStoreReplyQueryCriteria;
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
@Api(tags = "仓买 店铺管理")
@RequestMapping("/api/tsStoreReply")
public class TsStoreReplyController {

    private final TsStoreReplyService tsStoreReplyService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tsStoreReply:list')")
    public void exportTsStoreReply(HttpServletResponse response, TsStoreReplyQueryCriteria criteria) throws IOException {
        tsStoreReplyService.download(tsStoreReplyService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询仓买 店铺")
    @ApiOperation("查询仓买 店铺")
    @PreAuthorize("@el.check('tsStoreReply:list')")
    public ResponseEntity<Object> queryTsStoreReply(TsStoreReplyQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsStoreReplyService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增仓买 店铺")
    @ApiOperation("新增仓买 店铺")
    @PreAuthorize("@el.check('tsStoreReply:add')")
    public ResponseEntity<Object> createTsStoreReply(@Validated @RequestBody TsStoreReply resources){
        return new ResponseEntity<>(tsStoreReplyService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改仓买 店铺")
    @ApiOperation("修改仓买 店铺")
    @PreAuthorize("@el.check('tsStoreReply:edit')")
    public ResponseEntity<Object> updateTsStoreReply(@Validated @RequestBody TsStoreReply resources){
        tsStoreReplyService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除仓买 店铺")
    @ApiOperation("删除仓买 店铺")
    @PreAuthorize("@el.check('tsStoreReply:del')")
    public ResponseEntity<Object> deleteTsStoreReply(@RequestBody Long[] ids) {
        tsStoreReplyService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}