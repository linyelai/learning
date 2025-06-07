package com.linyelai.mapper;

import com.linyelai.dto.ShopQueryDTO;
import com.linyelai.po.ShopPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopMapper {

    int insert(ShopPO shop);
    int update(ShopPO shop);
    int deleteById(@Param("shopId") Long shopId);
    ShopPO selectById(@Param("shopId") Long shopId);
    List<ShopPO> selectAll();
    List<ShopPO> selectByCondition(ShopQueryDTO queryDTO);
    int countByCondition(ShopQueryDTO queryDTO);
    int countByShopName(@Param("shopName")String shopName);
}
