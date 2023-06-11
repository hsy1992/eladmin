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
import me.zhengjie.modules.store.domain.TsStoreSale;
import me.zhengjie.modules.store.service.TsStoreSaleService;
import me.zhengjie.modules.store.service.dto.TsStoreSaleQueryCriteria;
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
@Api(tags = "店铺销量管理")
@RequestMapping("/api/tsStoreSale")
public class TsStoreSaleController {

    private final TsStoreSaleService tsStoreSaleService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void exportTsStoreSale(HttpServletResponse response, TsStoreSaleQueryCriteria criteria) throws IOException {
        tsStoreSaleService.download(tsStoreSaleService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询店铺销量")
    @ApiOperation("查询店铺销量")
    public ResponseEntity<Object> queryTsStoreSale(TsStoreSaleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tsStoreSaleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增店铺销量")
    @ApiOperation("新增店铺销量")
    public ResponseEntity<Object> createTsStoreSale(@Validated @RequestBody TsStoreSale resources){
        return new ResponseEntity<>(tsStoreSaleService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改店铺销量")
    @ApiOperation("修改店铺销量")
    public ResponseEntity<Object> updateTsStoreSale(@Validated @RequestBody TsStoreSale resources){
        tsStoreSaleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除店铺销量")
    @ApiOperation("删除店铺销量")
    public ResponseEntity<Object> deleteTsStoreSale(@RequestBody Long[] ids) {
        tsStoreSaleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}