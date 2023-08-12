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

import me.zhengjie.modules.store.domain.TsStoreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
* @website https://eladmin.vip
* @author endless
* @date 2023-04-13
**/
public interface TsStoreOrderRepository extends JpaRepository<TsStoreOrder, Long>, JpaSpecificationExecutor<TsStoreOrder> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE ts_store_order set ts_store_order.status = :status where ts_store_order.id = :id", nativeQuery = true)
    Integer updateOrderStatus(@Param("id") Long id, @Param("status") int status);

    /**
     * 更改订单id
     * @param id
     * @param wxId
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE ts_store_order set ts_store_order.wx_id = :wx_id where ts_store_order.id = :id", nativeQuery = true)
    Integer updateWxId(@Param("id") Long id, @Param("wx_id") String wxId);

    /**
     * 根据wxid 更新订单状态
     * @param id
     * @param wxId
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE ts_store_order set ts_store_order.status = :status where ts_store_order.wx_id = :wx_id", nativeQuery = true)
    Integer updateStatusByWxId(@Param("status") int status, @Param("wx_id") String wxId);
}