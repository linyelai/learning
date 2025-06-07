package com.linyelai.service;

import com.linyelai.controller.response.PageResult;
import com.linyelai.controller.response.Result;
import com.linyelai.dto.ShopDTO;
import com.linyelai.dto.ShopQueryDTO;
import com.linyelai.vo.ShopVO;

public interface ShopService {

    Result<Long> createShop(ShopDTO shopDTO);
    Result<Boolean> updateShop(ShopDTO shopDTO);
    Result<Boolean> deleteShop(Long shopId);
    Result<ShopVO> getShopById(Long shopId);
    Result<PageResult<ShopVO>> queryShops(ShopQueryDTO queryDTO);
    Result<Boolean> changeShopStatus(Long shopId, Integer status);

}
