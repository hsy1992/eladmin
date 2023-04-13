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
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author endless
* @date 2023-04-13
**/
@Data
public class TsStoreDto implements Serializable {

    /** 是否删除;0 删除 1未删除 */
    private Boolean isDel;

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Timestamp createdTime;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private Timestamp updatedTime;

    /** 门店id;门店id */
    private Long id;

    /** 门店Code */
    private String code;

    /** 门店名称 */
    private String name;

    /** 门店电话 */
    private String phone;

    /** 门店地址省市区 */
    private String address;

    /** 门店详情 */
    private String addressDetails;

    /** 纬度 */
    private String latitude;

    /** 经度 */
    private String longitude;

    /** 门店logo */
    private String logo;

    /** 门头照片 */
    private String banner;

    /** 门店照片 */
    private String pics;

    /** 店铺介绍 */
    private String introduce;

    /** 营业时间 */
    private String dayTime;

    /** 搜索范围km */
    private Integer searchScope;

    /** 配送范围 */
    private Integer expressScope;

    /** 广告 */
    private String advertisement;
}