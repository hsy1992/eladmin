package me.zhengjie.modules.api.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 订单数据
 */
@Data
public class AppOrderDto {

    @NotBlank
    private String uid;

    private String remarks;

    @NotBlank
    private String userAddrId;

    private String expressPrice;

    private String totalPrice;

    private String userAddrName;

    private String userAddrInfo;

    private String userAddrPhone;

    private List<AppOrderProductDto> carList;

}
