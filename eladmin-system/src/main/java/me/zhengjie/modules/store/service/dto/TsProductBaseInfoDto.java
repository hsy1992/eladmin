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
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
* @website https://eladmin.vip
* @description /
* @author endless
* @date 2023-04-13
**/
@Data
public class TsProductBaseInfoDto implements Serializable {

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Timestamp createdTime;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private Timestamp updatedTime;

    private Boolean isDel;

    /** 商品条码(必填) */
    private String code;

    /** 商品名称(必填) */
    private String name;

    /** 商品简称(必填) */
    private String shortName;

    /** 规格型号 */
    private String model;

    /** 库存单位(必填) */
    private String size;

    /** 商品类别名称(必填) */
    private String cateName;

    /** 商品品牌名称(必填) */
    private String brandName;

    /** 供应商(必填) */
    private String supplier;

    /** 进货价 */
    private BigDecimal buyPrice;

    /** 销售价 */
    private BigDecimal salesPrice;

    /** 计价方式 */
    private String valuationMethod;

    /** 防止精度丢失 */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
}