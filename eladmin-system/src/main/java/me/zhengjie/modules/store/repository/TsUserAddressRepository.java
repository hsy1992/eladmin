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
package me.zhengjie.modules.store.repository;

import me.zhengjie.modules.store.domain.TsUserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author endless
 * @website https://eladmin.vip
 * @date 2023-04-13
 **/
public interface TsUserAddressRepository extends JpaRepository<TsUserAddress, Long>, JpaSpecificationExecutor<TsUserAddress> {

    @Transactional
    @Modifying
    @Query("UPDATE TsUserAddress address set address.isDefault = false where address.uid = :uid")
    Integer setDefaultAddress(@Param("uid") Long uid);

    @Transactional
    @Modifying
    @Query("UPDATE TsUserAddress address set address.isDefault = true where address.id = :id")
    Integer setDefaultAddressTrue(@Param("id") Long id);

    /**
     * 获取默认地址
     */
    TsUserAddress getTsUserAddressByUidAndIsDefault(Long uid, Boolean isDefault);
}