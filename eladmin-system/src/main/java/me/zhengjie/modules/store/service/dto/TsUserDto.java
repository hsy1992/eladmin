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
public class TsUserDto implements Serializable {

    private Boolean isDel;

    private Long id;

    /** 用户账户 */
    private String username;

    /** 用户密码 */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 生日 */
    private String birthday;

    /** 身份证号码 */
    private String idcard;

    /** 备注 */
    private String mark;

    /** 昵称 */
    private String nickName;

    /** 用户头像 */
    private String avatar;

    /** 手机号 */
    private String phone;

    /** 添加时间 */
    private Timestamp createTime;

    /** 最后登录时间 */
    private Timestamp lastLoginTime;

    /** 详细地址 */
    private String address;

    /** 用户登录类型 */
    private String loginType;

    /** 微信用户json信息 */
    private String wxProfile;

    /** 用户状态1正常0禁止 */
    private String status;
}