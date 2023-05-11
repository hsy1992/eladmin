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
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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
@Table(name="ts_product")
public class TsProduct implements Serializable {

    @ApiModelProperty(value = "创建人")
    @CreatedBy
    @Column(name = "`created_by`", updatable = false)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "`created_time`")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createdTime;

    @LastModifiedBy
    @Column(name = "`updated_by`")
    @ApiModelProperty(value = "更新人")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "`updated_time`")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedTime;

    @Column(name = "`is_del`")
    @ApiModelProperty(value = "isDel")
    private Boolean isDel;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`image`")
    @ApiModelProperty(value = "商品图片")
    private String image;

    @NotBlank
    @Column(name = "`name`")
    @ApiModelProperty(value = "商品名称")
    private String name;

    @Column(name = "`info`")
    @ApiModelProperty(value = "商品简介")
    private String info;

    @Column(name = "`bar_code`")
    @ApiModelProperty(value = "条码")
    private String barCode;

    @Column(name = "`keyword`")
    @ApiModelProperty(value = "关键字")
    private String keyword;

    @NotBlank
    @Column(name = "`cate_id`")
    @ApiModelProperty(value = "分类id")
    private String cateId;

    @Column(name = "`cate_name`")
    @ApiModelProperty(value = "分类名称")
    private String cateName;

    @Column(name = "`price`")
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @Column(name = "`sort`")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Column(name = "`is_show`")
    @ApiModelProperty(value = "是否展示")
    private String isShow;

    @Column(name = "`description`")
    @ApiModelProperty(value = "产品描述")
    private String description;

    @Column(name = "`ficti`")
    @ApiModelProperty(value = "虚拟销量")
    private Integer ficti;

    @Column(name = "`sales`")
    @ApiModelProperty(value = "销量")
    private Integer sales;

    @Column(name = "`store_id`")
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    public void copy(TsProduct source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
