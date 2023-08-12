package me.zhengjie.modules.api.domain;

import lombok.Data;

import java.util.List;

/**
 * 订单返回数据
 */
@Data
public class AppOrderResponse {

    private String remarks;

    private String expressPrice;

    private String totalPrice;

    private String userAddrName;

    private String userAddrInfo;

    private String userAddrPhone;

    private int status;

    private String orderId;

    private String createTime;

    private String shopName;

    private String wxId;

    private List<AppOrderProductDto> carList;
}
