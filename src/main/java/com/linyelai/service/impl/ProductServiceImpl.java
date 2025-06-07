package com.linyelai.service.impl;

import com.linyelai.mapper.ProductMapper;
import com.linyelai.mapper.ProductService;
import com.linyelai.po.ProductPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Cacheable(value = "products", key = "#id")
    public ProductPO getProductById(Long id) {
        // 模拟数据库查询
        return productMapper.findById(id).orElse(null);
    }

    @CachePut(value = "products", key = "#product.id")
    public ProductPO updateProduct(ProductPO product) {
        return productMapper.save(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void clearAllProductCache() {
        // 清除所有products缓存
    }
}
