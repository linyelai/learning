package com.linyelai.service.impl;

import com.linyelai.controller.response.PageResult;
import com.linyelai.controller.response.Result;
import com.linyelai.dto.ShopDTO;
import com.linyelai.dto.ShopQueryDTO;
import com.linyelai.mapper.ShopMapper;
import com.linyelai.po.ShopPO;
import com.linyelai.service.ShopService;
import com.linyelai.vo.ShopVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// ShopServiceImpl.java
@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Override
    @Transactional
    public Result<Long> createShop(ShopDTO shopDTO) {
        // 校验逻辑
        if (shopMapper.countByShopName(shopDTO.getShopName()) > 0) {
            return Result.fail("店铺名称已存在");
        }

        ShopPO shop = new ShopPO();
        BeanUtils.copyProperties(shopDTO, shop);
        shop.setShopStatus(2); // 默认审核中
        shop.setDeleted(false);
        shop.setCreatedTime(LocalDateTime.now());
        shop.setUpdatedTime(LocalDateTime.now());

        shopMapper.insert(shop);
        return Result.success(shop.getShopId());
    }

    @Override
    public Result<Boolean> updateShop(ShopDTO shopDTO) {
        return null;
    }

    @Override
    public Result<Boolean> deleteShop(Long shopId) {
        return null;
    }

    @Override
    public Result<ShopVO> getShopById(Long shopId) {
        return null;
    }

    @Override
    public Result<PageResult<ShopVO>> queryShops(ShopQueryDTO queryDTO) {
        // 分页参数处理
        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() <= 0) {
            queryDTO.setPageNum(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() <= 0) {
            queryDTO.setPageSize(10);
        }

        int total = shopMapper.countByCondition(queryDTO);
        if (total == 0) {
            return Result.success(new PageResult<>());
        }

        List<ShopPO> shops = shopMapper.selectByCondition(queryDTO);
        List<ShopVO> shopVOs = shops.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        PageResult<ShopVO> pageResult = new PageResult<>();
        pageResult.setList(shopVOs);
        pageResult.setTotal(total);
        pageResult.setPageNum(queryDTO.getPageNum());
        pageResult.setPageSize(queryDTO.getPageSize());

        return Result.success(pageResult);
    }

    @Override
    public Result<Boolean> changeShopStatus(Long shopId, Integer status) {
        return null;
    }

    // 其他方法实现...

    private ShopVO convertToVO(ShopPO shop) {
        ShopVO vo = new ShopVO();
        BeanUtils.copyProperties(shop, vo);
        // 转换状态为文字描述
        vo.setStatusName(getStatusName(shop.getShopStatus()));
        return vo;
    }

    private String getStatusName(Integer status) {
        switch (status) {
            case 0: return "已关闭";
            case 1: return "正常营业";
            case 2: return "审核中";
            case 3: return "已禁用";
            default: return "未知状态";
        }
    }
}