package com.example.ProductCatalogServiceProxy.service;

import com.example.ProductCatalogServiceProxy.dto.ProductDTO;

public interface IProductService {
    String getProducts();

    String getProduct(String productId);

    String createProduct(ProductDTO productDTO);

    String updateProduct(ProductDTO productDTO);
}
