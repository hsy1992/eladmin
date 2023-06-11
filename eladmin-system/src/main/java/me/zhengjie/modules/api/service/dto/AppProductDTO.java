package me.zhengjie.modules.api.service.dto;

import lombok.Data;
import me.zhengjie.modules.store.domain.TsProduct;

import java.util.HashMap;
import java.util.List;

@Data
public class AppProductDTO {

    private HashMap<String, List<TsProduct>> list;
}
