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
import me.zhengjie.modules.store.domain.TsUser;
import me.zhengjie.modules.store.service.TsUserService;
import me.zhengjie.modules.store.service.dto.TsUserQueryCriteria;
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
@Api(tags = "用户表管理")
@RequestMapping("/api/tsUser")
public class TsUserController {

    private final TsUserService tsUserService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tsUser:list')")
    public void exportTsUser(HttpServletResponse response, TsUserQueryCriteria criteria) throws IOException {
        tsUserService.download(tsUserService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户表")
    @ApiOperation("查询用户表")
    @PreAuthorize("@el.check('tsUser:list')")
    public ResponseEntity<Object> queryTsUser(TsUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增用户表")
    @ApiOperation("新增用户表")
    @PreAuthorize("@el.check('tsUser:add')")
    public ResponseEntity<Object> createTsUser(@Validated @RequestBody TsUser resources){
        return new ResponseEntity<>(tsUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改用户表")
    @ApiOperation("修改用户表")
    @PreAuthorize("@el.check('tsUser:edit')")
    public ResponseEntity<Object> updateTsUser(@Validated @RequestBody TsUser resources){
        tsUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除用户表")
    @ApiOperation("删除用户表")
    @PreAuthorize("@el.check('tsUser:del')")
    public ResponseEntity<Object> deleteTsUser(@RequestBody Long[] ids) {
        tsUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}