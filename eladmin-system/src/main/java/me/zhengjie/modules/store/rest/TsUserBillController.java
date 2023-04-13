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
import me.zhengjie.modules.store.domain.TsUserBill;
import me.zhengjie.modules.store.service.TsUserBillService;
import me.zhengjie.modules.store.service.dto.TsUserBillQueryCriteria;
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
@Api(tags = "用户订单管理")
@RequestMapping("/api/tsUserBill")
public class TsUserBillController {

    private final TsUserBillService tsUserBillService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tsUserBill:list')")
    public void exportTsUserBill(HttpServletResponse response, TsUserBillQueryCriteria criteria) throws IOException {
        tsUserBillService.download(tsUserBillService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户订单")
    @ApiOperation("查询用户订单")
    @PreAuthorize("@el.check('tsUserBill:list')")
    public ResponseEntity<Object> queryTsUserBill(TsUserBillQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsUserBillService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增用户订单")
    @ApiOperation("新增用户订单")
    @PreAuthorize("@el.check('tsUserBill:add')")
    public ResponseEntity<Object> createTsUserBill(@Validated @RequestBody TsUserBill resources){
        return new ResponseEntity<>(tsUserBillService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改用户订单")
    @ApiOperation("修改用户订单")
    @PreAuthorize("@el.check('tsUserBill:edit')")
    public ResponseEntity<Object> updateTsUserBill(@Validated @RequestBody TsUserBill resources){
        tsUserBillService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除用户订单")
    @ApiOperation("删除用户订单")
    @PreAuthorize("@el.check('tsUserBill:del')")
    public ResponseEntity<Object> deleteTsUserBill(@RequestBody Long[] ids) {
        tsUserBillService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}