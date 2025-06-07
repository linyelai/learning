package com.linyelai.controller;

import com.linyelai.controller.response.PageResult;
import com.linyelai.controller.response.Result;
import com.linyelai.dto.ShopDTO;
import com.linyelai.dto.ShopQueryDTO;
import com.linyelai.service.ShopService;
import com.linyelai.vo.ShopVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// ShopController.java
@RestController
@RequestMapping("/api/shops")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PostMapping
    public Result<Long> createShop(@RequestBody ShopDTO shopDTO) {
        return shopService.createShop(shopDTO);
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateShop(
            @RequestBody ShopDTO shopDTO) {
        return shopService.updateShop(shopDTO);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteShop(@PathVariable Long id) {
        return shopService.deleteShop(id);
    }

    @GetMapping("/{id}")
    public Result<ShopVO> getShopById(@PathVariable Long id) {
        return shopService.getShopById(id);
    }

    @GetMapping
    public Result<PageResult<ShopVO>> queryShops(ShopQueryDTO queryDTO) {
        return shopService.queryShops(queryDTO);
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> changeShopStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        return shopService.changeShopStatus(id, status);
    }
}