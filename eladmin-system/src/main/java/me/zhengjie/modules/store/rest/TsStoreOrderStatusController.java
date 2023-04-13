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
import me.zhengjie.modules.store.domain.TsStoreOrderStatus;
import me.zhengjie.modules.store.service.TsStoreOrderStatusService;
import me.zhengjie.modules.store.service.dto.TsStoreOrderStatusQueryCriteria;
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
@Api(tags = "订单操作记录管理")
@RequestMapping("/api/tsStoreOrderStatus")
public class TsStoreOrderStatusController {

    private final TsStoreOrderStatusService tsStoreOrderStatusService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tsStoreOrderStatus:list')")
    public void exportTsStoreOrderStatus(HttpServletResponse response, TsStoreOrderStatusQueryCriteria criteria) throws IOException {
        tsStoreOrderStatusService.download(tsStoreOrderStatusService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询订单操作记录")
    @ApiOperation("查询订单操作记录")
    @PreAuthorize("@el.check('tsStoreOrderStatus:list')")
    public ResponseEntity<Object> queryTsStoreOrderStatus(TsStoreOrderStatusQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsStoreOrderStatusService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增订单操作记录")
    @ApiOperation("新增订单操作记录")
    @PreAuthorize("@el.check('tsStoreOrderStatus:add')")
    public ResponseEntity<Object> createTsStoreOrderStatus(@Validated @RequestBody TsStoreOrderStatus resources){
        return new ResponseEntity<>(tsStoreOrderStatusService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改订单操作记录")
    @ApiOperation("修改订单操作记录")
    @PreAuthorize("@el.check('tsStoreOrderStatus:edit')")
    public ResponseEntity<Object> updateTsStoreOrderStatus(@Validated @RequestBody TsStoreOrderStatus resources){
        tsStoreOrderStatusService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除订单操作记录")
    @ApiOperation("删除订单操作记录")
    @PreAuthorize("@el.check('tsStoreOrderStatus:del')")
    public ResponseEntity<Object> deleteTsStoreOrderStatus(@RequestBody Long[] ids) {
        tsStoreOrderStatusService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}