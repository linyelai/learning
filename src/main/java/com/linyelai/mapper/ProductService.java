package com.linyelai.mapper;

import com.linyelai.po.ProductPO;

public interface ProductService {


    public ProductPO getProductById(Long id);


    public ProductPO updateProduct(ProductPO product) ;


    public void deleteProduct(Long id) ;


    public void clearAllProductCache() ;
}
