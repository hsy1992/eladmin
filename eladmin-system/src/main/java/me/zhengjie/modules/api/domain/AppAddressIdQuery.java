package me.zhengjie.modules.api.domain;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class AppAddressIdQuery {

    @Query(type = Query.Type.EQUAL)
    private Long id;

    private Long uid;
}
