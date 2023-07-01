package me.zhengjie.modules.api.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.api.domain.AppAddressIdQuery;
import me.zhengjie.modules.api.domain.AppResultBean;
import me.zhengjie.modules.api.domain.AppUserIdQuery;
import me.zhengjie.modules.api.service.AppApiService;
import me.zhengjie.modules.api.service.dto.HomeStoreRequest;
import me.zhengjie.modules.api.service.dto.ProductListByStoreRequest;
import me.zhengjie.modules.store.domain.TsUser;
import me.zhengjie.modules.store.service.TsProductClassifyService;
import me.zhengjie.modules.store.service.dto.TsProductClassifyQueryCriteria;
import me.zhengjie.modules.store.service.dto.TsUserAddressDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * api
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "app 接口服务")
@RequestMapping("/app")
public class AppApiController {

    private final AppApiService appService;
    private final TsProductClassifyService tsProductClassifyService;

    @AnonymousPostMapping("/user/create")
    @Log("创建新用户")
    @ApiOperation("创建新用户")
    public ResponseEntity<Object> login(@RequestBody TsUser tsUser) {
        return new ResponseEntity<>(appService.login(tsUser), HttpStatus.OK);
    }

    @AnonymousPostMapping("/home/storeList")
    @Log("查询首页商品")
    @ApiOperation("查询首页商品")
    public ResponseEntity<Object> queryHomeStoreList(@RequestBody HomeStoreRequest request) {
        return new ResponseEntity<>(appService.queryHomeStoreList(request.getLat(), request.getLng()), HttpStatus.OK);
    }

    @AnonymousPostMapping("/store/cateByStore")
    @Log("查询分类")
    @ApiOperation("查询分类")
    public ResponseEntity<Object> queryCateByStore(@RequestBody ProductListByStoreRequest request) {
        return new ResponseEntity<>(appService.queryCateByStore(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/store/queryExpress")
    @Log("查询邮费")
    @ApiOperation("查询邮费")
    public ResponseEntity<Object> queryExpress(@RequestBody ProductListByStoreRequest request) {
        return new ResponseEntity<>(appService.queryExpress(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/address/queryAddressByUser")
    @Log("查询用户地址")
    @ApiOperation("查询用户地址")
    public ResponseEntity<Object> queryAddressByUser(@RequestBody AppUserIdQuery request) {
        return new ResponseEntity<>(appService.queryAddressByUser(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/address/queryDefaultAddress")
    @Log("查询默认地址")
    @ApiOperation("查询默认地址")
    public ResponseEntity<Object> queryDefaultAddress(@RequestBody AppUserIdQuery request) {
        return new ResponseEntity<>(appService.queryDefaultAddress(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/address/queryAddressById")
    @Log("查询地址根据地址id")
    @ApiOperation("查询地址根据地址id")
    public ResponseEntity<Object> queryAddressById(@RequestBody AppAddressIdQuery request) {
        return new ResponseEntity<>(appService.queryAddressById(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/address/saveAddress")
    @Log("保存地址")
    @ApiOperation("保存地址")
    public ResponseEntity<Object> saveAddress(@RequestBody TsUserAddressDto request) {
        return new ResponseEntity<>(appService.saveAddress(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/address/updateAddress")
    @Log("修改地址")
    @ApiOperation("修改地址")
    public ResponseEntity<Object> updateAddress(@RequestBody TsUserAddressDto request) {
        return new ResponseEntity<>(appService.updateAddress(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/address/setDefaultAddress")
    @Log("设置默认地址")
    @ApiOperation("设置默认地址")
    public ResponseEntity<Object> setDefaultAddress(@RequestBody AppAddressIdQuery request) {
        return new ResponseEntity<>(appService.setDefaultAddress(request), HttpStatus.OK);
    }
}
