package me.zhengjie.modules.api.service;

import me.zhengjie.modules.api.domain.AppAddressIdQuery;
import me.zhengjie.modules.api.domain.AppResultBean;
import me.zhengjie.modules.api.domain.AppStoreProductDTO;
import me.zhengjie.modules.api.domain.AppUserIdQuery;
import me.zhengjie.modules.api.service.dto.HomeStoreDTO;
import me.zhengjie.modules.api.service.dto.ProductListByStoreRequest;
import me.zhengjie.modules.store.domain.TsProduct;
import me.zhengjie.modules.store.domain.TsStoreExpress;
import me.zhengjie.modules.store.domain.TsUser;
import me.zhengjie.modules.store.domain.TsUserAddress;
import me.zhengjie.modules.store.service.dto.TsStoreExpressDto;
import me.zhengjie.modules.store.service.dto.TsUserAddressDto;

import java.util.List;
import java.util.Map;

public interface AppApiService {

    AppResultBean<Object> login(TsUser tsUser);

    AppResultBean<HomeStoreDTO> queryHomeStoreList(String lat, String lng);

    AppResultBean<List<AppStoreProductDTO>> queryCateByStore(ProductListByStoreRequest request);

    AppResultBean<TsStoreExpress> queryExpress(ProductListByStoreRequest request);

    AppResultBean<List<TsUserAddressDto>> queryAddressByUser(AppUserIdQuery request);

    AppResultBean<TsUserAddressDto> queryAddressById(AppAddressIdQuery request);

    AppResultBean<Object> saveAddress(TsUserAddressDto request);

    AppResultBean<Object> updateAddress(TsUserAddressDto request);

    AppResultBean<Object> setDefaultAddress(AppAddressIdQuery request);

    AppResultBean<TsUserAddress> queryDefaultAddress(AppUserIdQuery request);
}
