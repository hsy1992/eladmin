package me.zhengjie.modules.api.rest;

import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.api.domain.*;
import me.zhengjie.modules.api.service.AppApiService;
import me.zhengjie.modules.api.service.dto.HomeStoreRequest;
import me.zhengjie.modules.api.service.dto.ProductListByStoreRequest;
import me.zhengjie.modules.store.domain.TsUser;
import me.zhengjie.modules.store.service.TsProductClassifyService;
import me.zhengjie.modules.store.service.dto.TsProductClassifyQueryCriteria;
import me.zhengjie.modules.store.service.dto.TsUserAddressDto;
import me.zhengjie.tools.PayTool;
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

    @AnonymousPostMapping("/order/submit")
    @Log("提交订单")
    @ApiOperation("提交订单")
    public ResponseEntity<Object> submitOrder(@RequestBody AppOrderDto request) {
        return new ResponseEntity<>(appService.submitOrder(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/order/list")
    @Log("订单列表")
    @ApiOperation("订单列表")
    public ResponseEntity<Object> orderList(@RequestBody AppOrderListRequest request) {
        return new ResponseEntity<>(appService.orderList(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/order/details")
    @Log("订单详情")
    @ApiOperation("订单详情")
    public ResponseEntity<Object> orderDetails(@RequestBody AppOrderListRequest request) {
        return new ResponseEntity<>(appService.orderDetails(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/order/pay")
    @Log("订单支付")
    @ApiOperation("订单支付")
    public ResponseEntity<Object> orderPay(@RequestBody AppOrderListRequest request) {
        return new ResponseEntity<>(appService.orderPay(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/order/cancel")
    @Log("取消订单")
    @ApiOperation("取消订单")
    public ResponseEntity<Object> orderCancel(@RequestBody AppOrderListRequest request) {
        request.setStatus(4);
        return new ResponseEntity<>(appService.orderEditStatus(request), HttpStatus.OK);
    }

    @AnonymousPostMapping("/order/confirm")
    @Log("确认收货")
    @ApiOperation("确认收货")
    public ResponseEntity<Object> orderConfirm(@RequestBody AppOrderListRequest request) {
        request.setStatus(3);
        return new ResponseEntity<>(appService.orderEditStatus(request), HttpStatus.OK);
    }


    @AnonymousPostMapping("/result")
    @Log("支付通知")
    @ApiOperation("支付通知")
    public ResponseEntity<Object> payResult(@RequestBody RequestParam requestParam) {
        return new ResponseEntity<>(appService.payResult(requestParam), HttpStatus.OK);
    }
}
