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
package me.zhengjie.modules.store.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author endless
* @date 2023-04-13
**/
@Entity
@Data
@Table(name="ts_store_order")
public class TsStoreOrder implements Serializable {

    @Column(name = "`create_time`")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "`update_time`")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`code`")
    @ApiModelProperty(value = "订单号")
    private String code;

    @Column(name = "`user_id`")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @Column(name = "`real_name`")
    @ApiModelProperty(value = "用户名")
    private String realName;

    @Column(name = "`user_phone`")
    @ApiModelProperty(value = "用户电话")
    private String userPhone;

    @Column(name = "`user_address`")
    @ApiModelProperty(value = "用户地址")
    private String userAddress;

    @Column(name = "`cart_id`")
    @ApiModelProperty(value = "购物车id")
    private Long cartId;

    @Column(name = "`express_price`")
    @ApiModelProperty(value = "运费金额")
    private BigDecimal expressPrice;

    @Column(name = "`total_num`")
    @ApiModelProperty(value = "商品总数")
    private Integer totalNum;

    @Column(name = "`total_price`")
    @ApiModelProperty(value = "订单总价")
    private BigDecimal totalPrice;

    @Column(name = "`pay_price`")
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payPrice;

    @Column(name = "`paid`")
    @ApiModelProperty(value = "支付状态")
    private Integer paid;

    @Column(name = "`pay_time`")
    @ApiModelProperty(value = "支付时间")
    private Timestamp payTime;

    @Column(name = "`pay_type`")
    @ApiModelProperty(value = "支付方式")
    private String payType;

    @Column(name = "`status`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "-1申请退款 -2 退款成功 0 待发货 1待收货 2 已收货 3已完成")
    private Integer status;

    @Column(name = "`remark`")
    @ApiModelProperty(value = "备注")
    private String remark;

    @Column(name = "`is_del`")
    @ApiModelProperty(value = "isDel")
    private Boolean isDel;

    @Column(name = "`shipping_type`")
    @ApiModelProperty(value = "1配送 2 自提")
    private Integer shippingType;

    @Column(name = "`store_id`")
    @ApiModelProperty(value = "门店id")
    private Long storeId;

    @Column(name = "`unique`")
    @ApiModelProperty(value = "唯一id")
    private String unique;

    @Column(name = "`wx_id`")
    @ApiModelProperty(value = "微信订单id")
    private String wxId;

    public void copy(TsStoreOrder source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
