package com.example.ProductCatalogServiceProxy.service;

import com.example.ProductCatalogServiceProxy.dto.ProductDTO;
import com.example.ProductCatalogServiceProxy.model.Product;

import java.util.List;

public interface IProductService {
    List<Product> getProducts();

    Product getProduct(Long productId);

    Product createProduct(ProductDTO productDTO);

    String updateProduct(ProductDTO productDTO);
}
