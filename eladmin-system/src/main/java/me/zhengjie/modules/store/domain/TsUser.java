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
@Table(name="ts_user")
public class TsUser implements Serializable {

    @Column(name = "`is_del`")
    @ApiModelProperty(value = "isDel")
    private Boolean isDel;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`username`")
    @ApiModelProperty(value = "用户账户")
    private String username;

    @Column(name = "`password`")
    @ApiModelProperty(value = "用户密码")
    private String password;

    @Column(name = "`real_name`")
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @Column(name = "`birthday`")
    @ApiModelProperty(value = "生日")
    private String birthday;

    @Column(name = "`idcard`")
    @ApiModelProperty(value = "身份证号码")
    private String idcard;

    @Column(name = "`mark`")
    @ApiModelProperty(value = "备注")
    private String mark;

    @Column(name = "`nick_name`")
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @Column(name = "`avatar`")
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @Column(name = "`phone`")
    @ApiModelProperty(value = "手机号")
    private String phone;

    @Column(name = "`create_time`")
    @ApiModelProperty(value = "添加时间")
    private Timestamp createTime;

    @Column(name = "`last_login_time`")
    @ApiModelProperty(value = "最后登录时间")
    private Timestamp lastLoginTime;

    @Column(name = "`address`")
    @ApiModelProperty(value = "详细地址")
    private String address;

    @Column(name = "`login_type`")
    @ApiModelProperty(value = "用户登录类型")
    private String loginType;

    @Column(name = "`wx_profile`")
    @ApiModelProperty(value = "微信用户json信息")
    private String wxProfile;

    @Column(name = "`status`")
    @ApiModelProperty(value = "用户状态1正常0禁止")
    private String status;

    public void copy(TsUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
