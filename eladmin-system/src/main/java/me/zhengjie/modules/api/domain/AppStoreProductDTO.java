package me.zhengjie.modules.api.domain;

import lombok.Data;
import me.zhengjie.modules.store.domain.TsProduct;

import java.util.List;

@Data
public class AppStoreProductDTO {

    private String cateName;

    private List<TsProduct> list;
}
