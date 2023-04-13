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
import me.zhengjie.modules.store.domain.TsStoreOrder;
import me.zhengjie.modules.store.service.TsStoreOrderService;
import me.zhengjie.modules.store.service.dto.TsStoreOrderQueryCriteria;
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
@Api(tags = "订单管理")
@RequestMapping("/api/tsStoreOrder")
public class TsStoreOrderController {

    private final TsStoreOrderService tsStoreOrderService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tsStoreOrder:list')")
    public void exportTsStoreOrder(HttpServletResponse response, TsStoreOrderQueryCriteria criteria) throws IOException {
        tsStoreOrderService.download(tsStoreOrderService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询订单")
    @ApiOperation("查询订单")
    @PreAuthorize("@el.check('tsStoreOrder:list')")
    public ResponseEntity<Object> queryTsStoreOrder(TsStoreOrderQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsStoreOrderService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增订单")
    @ApiOperation("新增订单")
    @PreAuthorize("@el.check('tsStoreOrder:add')")
    public ResponseEntity<Object> createTsStoreOrder(@Validated @RequestBody TsStoreOrder resources){
        return new ResponseEntity<>(tsStoreOrderService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改订单")
    @ApiOperation("修改订单")
    @PreAuthorize("@el.check('tsStoreOrder:edit')")
    public ResponseEntity<Object> updateTsStoreOrder(@Validated @RequestBody TsStoreOrder resources){
        tsStoreOrderService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除订单")
    @ApiOperation("删除订单")
    @PreAuthorize("@el.check('tsStoreOrder:del')")
    public ResponseEntity<Object> deleteTsStoreOrder(@RequestBody Long[] ids) {
        tsStoreOrderService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}