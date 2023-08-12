package me.zhengjie.modules.api.domain;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.util.List;

@Data
public class AppCarInfoQueryCriteria {

    @Query(type = Query.Type.IN)
    private List<Long> orderId;
}
