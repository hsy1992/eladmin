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
import me.zhengjie.exception.handler.ApiError;
import me.zhengjie.modules.store.domain.TsProduct;
import me.zhengjie.modules.store.service.TsProductService;
import me.zhengjie.modules.store.service.dto.TsProductBaseInfoDto;
import me.zhengjie.modules.store.service.dto.TsProductQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * @author endless
 * @website https://eladmin.vip
 * @date 2023-04-13
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "商品管理")
@RequestMapping("/api/tsProduct")
public class TsProductController {

    private final TsProductService tsProductService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void exportTsProduct(HttpServletResponse response, TsProductQueryCriteria criteria) throws IOException {
        tsProductService.download(tsProductService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询商品")
    @ApiOperation("查询商品")
    public ResponseEntity<Object> queryTsProduct(TsProductQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(tsProductService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增商品")
    @ApiOperation("新增商品")
    public ResponseEntity<Object> createTsProduct(@Validated @RequestBody TsProduct resources) {
        if (resources.getPrice() == null) {
            return  new ResponseEntity<>(ApiError.error("请输入售价"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tsProductService.create(resources), HttpStatus.CREATED);
    }

    @PostMapping("/insertList")
    @Log("批量新增商品")
    @ApiOperation("新增商品")
    public ResponseEntity<Object> createTsProductList(@RequestBody List<TsProductBaseInfoDto> resources) {
        tsProductService.createList(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @Log("修改商品")
    @ApiOperation("修改商品")
    public ResponseEntity<Object> updateTsProduct(@Validated @RequestBody TsProduct resources) {
        tsProductService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除商品")
    @ApiOperation("删除商品")
    public ResponseEntity<Object> deleteTsProduct(@RequestBody Long[] ids) {
        tsProductService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}