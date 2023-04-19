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
import me.zhengjie.base.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author endless
* @date 2023-04-13
**/
@Entity
@Data
@Table(name="ts_store")
@EntityListeners(AuditingEntityListener.class)
public class TsStore implements Serializable {

    @Column(name = "`is_del`")
    @ApiModelProperty(value = "是否删除;0 删除 1未删除")
    private Boolean isDel;

    @CreatedBy
    @Column(name = "`created_by`", updatable = false)
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "`created_time`", updatable = false)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Timestamp createdTime;

    @LastModifiedBy
    @Column(name = "`updated_by`", updatable = true)
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "`updated_time`", updatable = true)
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Timestamp updatedTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "门店id;门店id")
    private Long id;

    @Column(name = "`code`")
    @ApiModelProperty(value = "门店Code")
    private String code;

    @NotBlank(message = "密码为空")
    @Column(name = "`name`")
    @ApiModelProperty(value = "门店名称")
    private String name;

    @NotBlank
    @Column(name = "`phone`")
    @ApiModelProperty(value = "门店电话")
    private String phone;

    @NotBlank
    @Column(name = "`address`")
    @ApiModelProperty(value = "门店地址省市区")
    private String address;

    @NotBlank
    @Column(name = "`address_details`")
    @ApiModelProperty(value = "门店详情")
    private String addressDetails;

    @Column(name = "`latitude`")
    @ApiModelProperty(value = "纬度")
    private String latitude;

    @Column(name = "`longitude`")
    @ApiModelProperty(value = "经度")
    private String longitude;

    @Column(name = "`logo`")
    @ApiModelProperty(value = "门店logo")
    private String logo;

    @Column(name = "`banner`")
    @ApiModelProperty(value = "门头照片")
    private String banner;

    @Column(name = "`pics`")
    @ApiModelProperty(value = "门店照片")
    private String pics;

    @Column(name = "`introduce`")
    @ApiModelProperty(value = "店铺介绍")
    private String introduce;

    @Column(name = "`day_time`")
    @ApiModelProperty(value = "营业时间")
    private String dayTime;

    @Column(name = "`search_scope`")
    @ApiModelProperty(value = "搜索范围km")
    private Integer searchScope;

    @Column(name = "`express_scope`")
    @ApiModelProperty(value = "配送范围")
    private Integer expressScope;

    @Column(name = "`advertisement`")
    @ApiModelProperty(value = "广告")
    private String advertisement;

    public void copy(TsStore source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
