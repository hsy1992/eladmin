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
package me.zhengjie.modules.store.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author endless
* @date 2023-04-13
**/
@Data
public class TsStoreOrderDto implements Serializable {

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    private Long id;

    /** 订单号 */
    private String code;

    /** 用户id */
    private Long userId;

    /** 用户名 */
    private String realName;

    /** 用户电话 */
    private String userPhone;

    /** 用户地址 */
    private String userAddress;

    /** 购物车id */
    private Long cartId;

    /** 运费金额 */
    private BigDecimal expressPrice;

    /** 商品总数 */
    private Integer totalNum;

    /** 订单总价 */
    private BigDecimal totalPrice;

    /** 支付金额 */
    private BigDecimal payPrice;

    /** 支付状态 */
    private Integer paid;

    /** 支付时间 */
    private Timestamp payTime;

    /** 支付方式 */
    private String payType;

    /** -1申请退款 -2 退款成功 0 待发货 1待收货 2 已收货 3已完成 */
    private Integer status;

    /** 备注 */
    private String remark;

    private Boolean isDel;

    /** 1配送 2 自提 */
    private Integer shippingType;

    /** 门店id */
    private Long storeId;

    /** 唯一id */
    private String unique;

    /** 微信订单id */
    private String wxId;
}