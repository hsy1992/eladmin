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
@Table(name="ts_user_address")
public class TsUserAddress implements Serializable {

    @Column(name = "`is_del`")
    @ApiModelProperty(value = "isDel")
    private Boolean isDel;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`uid`")
    @ApiModelProperty(value = "用户id")
    private Long uid;

    @Column(name = "`real_name`")
    @ApiModelProperty(value = "收货人")
    private String realName;

    @Column(name = "`phone`")
    @ApiModelProperty(value = "电话")
    private String phone;

    @Column(name = "`province`")
    @ApiModelProperty(value = "省")
    private String province;

    @Column(name = "`city`")
    @ApiModelProperty(value = "市")
    private String city;

    @Column(name = "`district`")
    @ApiModelProperty(value = "区")
    private String district;

    @Column(name = "`details`")
    @ApiModelProperty(value = "详细地址")
    private String details;

    @Column(name = "`post_code`")
    @ApiModelProperty(value = "邮编")
    private String postCode;

    @Column(name = "`is_default`")
    @ApiModelProperty(value = "是否默认")
    private Boolean isDefault;

    @Column(name = "`create_time`")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "`update_time`")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    public void copy(TsUserAddress source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
