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
@Table(name="ts_product_base_info")
public class TsProductBaseInfo implements Serializable {

    @Column(name = "`created_by`")
    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @Column(name = "`created_time`")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createdTime;

    @Column(name = "`updated_by`")
    @ApiModelProperty(value = "更新人")
    private String updatedBy;

    @Column(name = "`updated_time`")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedTime;

    @Column(name = "`is_del`")
    @ApiModelProperty(value = "isDel")
    private Boolean isDel;

    @Column(name = "`code`")
    @ApiModelProperty(value = "商品条码(必填)")
    private String code;

    @Column(name = "`name`")
    @ApiModelProperty(value = "商品名称(必填)")
    private String name;

    @Column(name = "`short_name`")
    @ApiModelProperty(value = "商品简称(必填)")
    private String shortName;

    @Column(name = "`model`")
    @ApiModelProperty(value = "规格型号")
    private String model;

    @Column(name = "`size`")
    @ApiModelProperty(value = "库存单位(必填)")
    private String size;

    @Column(name = "`cate_name`")
    @ApiModelProperty(value = "商品类别名称(必填)")
    private String cateName;

    @Column(name = "`brand_name`")
    @ApiModelProperty(value = "商品品牌名称(必填)")
    private String brandName;

    @Column(name = "`supplier`")
    @ApiModelProperty(value = "供应商(必填)")
    private String supplier;

    @Column(name = "`buy_price`")
    @ApiModelProperty(value = "进货价")
    private BigDecimal buyPrice;

    @Column(name = "`sales_price`")
    @ApiModelProperty(value = "销售价")
    private String salesPrice;

    @Column(name = "`valuation_method`")
    @ApiModelProperty(value = "计价方式")
    private String valuationMethod;

    @Id
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    public void copy(TsProductBaseInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
