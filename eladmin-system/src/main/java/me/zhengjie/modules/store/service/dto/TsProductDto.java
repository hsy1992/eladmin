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
public class TsProductDto implements Serializable {

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Timestamp createdTime;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private Timestamp updatedTime;

    private Boolean isDel;

    private Long id;

    /** 商品图片 */
    private String image;

    /** 商品名称 */
    private String name;

    /** 商品简介 */
    private String info;

    /** 条码 */
    private String barCode;

    /** 关键字 */
    private String keyword;

    /** 分类id */
    private String cateId;

    /** 价格 */
    private BigDecimal price;

    /** 排序 */
    private Integer sort;

    /** 是否展示 */
    private String isShow;

    /** 产品描述 */
    private String description;

    /** 虚拟销量 */
    private Integer ficti;

    /** 销量 */
    private Integer sales;

    /** 店铺id */
    private Long storeId;
}