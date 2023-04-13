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
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author endless
* @date 2023-04-13
**/
@Entity
@Data
@Table(name="ts_product_classify")
public class TsProductClassify implements Serializable {

    @Column(name = "`is_del`")
    @ApiModelProperty(value = "isDel")
    private Boolean isDel;

    @Column(name = "`created_time`")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createdTime;

    @Column(name = "`updated_time`")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`pid`")
    @ApiModelProperty(value = "父id")
    private Long pid;

    @Column(name = "`cate_name`")
    @ApiModelProperty(value = "分类名称")
    private String cateName;

    @Column(name = "`sort`")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Column(name = "`icon`")
    @ApiModelProperty(value = "图标")
    private String icon;

    @Column(name = "`is_show`")
    @ApiModelProperty(value = "是否推荐")
    private Integer isShow;

    public void copy(TsProductClassify source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
