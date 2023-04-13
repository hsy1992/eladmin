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
@Table(name="ts_user_bill")
public class TsUserBill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`is_del`")
    @ApiModelProperty(value = "isDel")
    private Boolean isDel;

    @Column(name = "`uid`")
    @ApiModelProperty(value = "用户id")
    private Long uid;

    @Column(name = "`link_id`")
    @ApiModelProperty(value = "关联id")
    private String linkId;

    @Column(name = "`pm`")
    @ApiModelProperty(value = "0支出 1获得")
    private Boolean pm;

    @Column(name = "`title`")
    @ApiModelProperty(value = "标题")
    private String title;

    @Column(name = "`category`")
    @ApiModelProperty(value = "明细种类")
    private String category;

    @Column(name = "`type`")
    @ApiModelProperty(value = "明细类型")
    private String type;

    @Column(name = "`number`")
    @ApiModelProperty(value = "明细数字")
    private BigDecimal number;

    @Column(name = "`mark`")
    @ApiModelProperty(value = "备注")
    private String mark;

    @Column(name = "`create_time`")
    @ApiModelProperty(value = "createTime")
    private Timestamp createTime;

    @Column(name = "`update_time`")
    @ApiModelProperty(value = "updateTime")
    private Timestamp updateTime;

    @Column(name = "`status`")
    @ApiModelProperty(value = "0 待确认 1有效 -1无效")
    private Integer status;

    @Column(name = "`store_id`")
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    public void copy(TsUserBill source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
