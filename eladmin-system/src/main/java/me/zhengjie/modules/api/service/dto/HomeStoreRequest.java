package me.zhengjie.modules.api.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 首页商品请求参数
 */
@Data
public class HomeStoreRequest implements Serializable {
    private String lat;

    private String lng;
}
