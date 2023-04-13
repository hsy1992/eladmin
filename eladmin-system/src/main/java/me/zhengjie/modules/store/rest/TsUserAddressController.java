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
import me.zhengjie.modules.store.domain.TsUserAddress;
import me.zhengjie.modules.store.service.TsUserAddressService;
import me.zhengjie.modules.store.service.dto.TsUserAddressQueryCriteria;
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
@Api(tags = "用户地址管理")
@RequestMapping("/api/tsUserAddress")
public class TsUserAddressController {

    private final TsUserAddressService tsUserAddressService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tsUserAddress:list')")
    public void exportTsUserAddress(HttpServletResponse response, TsUserAddressQueryCriteria criteria) throws IOException {
        tsUserAddressService.download(tsUserAddressService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户地址")
    @ApiOperation("查询用户地址")
    @PreAuthorize("@el.check('tsUserAddress:list')")
    public ResponseEntity<Object> queryTsUserAddress(TsUserAddressQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsUserAddressService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增用户地址")
    @ApiOperation("新增用户地址")
    @PreAuthorize("@el.check('tsUserAddress:add')")
    public ResponseEntity<Object> createTsUserAddress(@Validated @RequestBody TsUserAddress resources){
        return new ResponseEntity<>(tsUserAddressService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改用户地址")
    @ApiOperation("修改用户地址")
    @PreAuthorize("@el.check('tsUserAddress:edit')")
    public ResponseEntity<Object> updateTsUserAddress(@Validated @RequestBody TsUserAddress resources){
        tsUserAddressService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除用户地址")
    @ApiOperation("删除用户地址")
    @PreAuthorize("@el.check('tsUserAddress:del')")
    public ResponseEntity<Object> deleteTsUserAddress(@RequestBody Long[] ids) {
        tsUserAddressService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}