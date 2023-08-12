package me.zhengjie.modules.api.domain;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class AppOrderListRequest {

    // 订单id
    @Query(type = Query.Type.EQUAL)
    private Long id;

    // 用户id
    @Query(type = Query.Type.EQUAL)
    private Long userId;

    // 状态
    @Query(type = Query.Type.EQUAL)
    private Integer status;
}
