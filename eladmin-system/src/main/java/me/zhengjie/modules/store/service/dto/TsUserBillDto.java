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
public class TsUserBillDto implements Serializable {

    private Long id;

    private Boolean isDel;

    /** 用户id */
    private Long uid;

    /** 关联id */
    private String linkId;

    /** 0支出 1获得 */
    private Boolean pm;

    /** 标题 */
    private String title;

    /** 明细种类 */
    private String category;

    /** 明细类型 */
    private String type;

    /** 明细数字 */
    private BigDecimal number;

    /** 备注 */
    private String mark;

    private Timestamp createTime;

    private Timestamp updateTime;

    /** 0 待确认 1有效 -1无效 */
    private Integer status;

    /** 店铺id */
    private Long storeId;
}