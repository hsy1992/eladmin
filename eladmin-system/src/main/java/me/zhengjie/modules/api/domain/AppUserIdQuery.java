package me.zhengjie.modules.api.domain;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class AppUserIdQuery {

    @Query(type = Query.Type.EQUAL)
    private Long uid;


    @Query(type = Query.Type.EQUAL)
    private Boolean isDel = Boolean.FALSE;
}
