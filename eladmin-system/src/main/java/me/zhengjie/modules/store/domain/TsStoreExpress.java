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
@Table(name="ts_store_express")
public class TsStoreExpress implements Serializable {

    @Column(name = "`create_by`")
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @Column(name = "`create_time`")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "`update_by`")
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @Column(name = "`update_time`")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Column(name = "`is_del`")
    @ApiModelProperty(value = "是否删除;0 失效 1有效")
    private Boolean isDel;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`express_price`")
    @ApiModelProperty(value = "运费")
    private BigDecimal expressPrice;

    @Column(name = "`free_express_price`")
    @ApiModelProperty(value = "满多少免运费")
    private BigDecimal freeExpressPrice;

    @Column(name = "`store_id`")
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    public void copy(TsStoreExpress source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
