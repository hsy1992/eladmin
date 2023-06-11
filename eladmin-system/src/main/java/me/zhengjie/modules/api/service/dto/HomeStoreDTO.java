package me.zhengjie.modules.api.service.dto;

import lombok.Data;
import me.zhengjie.modules.api.domain.StoreInfoDTO;

import java.util.List;


@Data
public class HomeStoreDTO {

    private List<StoreInfoDTO> nearList;

    private List<StoreInfoDTO> recommendList;
}
