package me.zhengjie.modules.api.domain;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class AppUserQueryCriteria {

    @Query(type = Query.Type.EQUAL)
    private String username;
}
