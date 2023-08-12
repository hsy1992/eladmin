package me.zhengjie.modules.api.domain;

import lombok.Data;

@Data
public class AppOrderProductDto {

    private String barCode;

    private String name;

    private String id;

    private String price;

    private int num;

    private Long storeId;

}
