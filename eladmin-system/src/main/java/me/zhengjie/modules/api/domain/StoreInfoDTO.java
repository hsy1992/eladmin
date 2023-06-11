package me.zhengjie.modules.api.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreInfoDTO {

    private Long id;
    private String name;

    private String phone;

    private String addressDetails;

    private String logo;

    private String introduce;

    private String dayTime;

    private String advertisement;

    private Double distance;

    private Integer searchScope;

}
