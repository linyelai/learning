package com.linyelai.mapper;

import com.linyelai.po.ProductPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ProductMapper {
    Optional<ProductPO> findById(Long id);

    ProductPO save(ProductPO product);

    void deleteById(Long id);
}
