package me.zhengjie.modules.api.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class AppProductQueryCriteria {

    @Query(type = Query.Type.EQUAL)
    private long storeId;

}
