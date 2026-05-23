package com.example.ProductCatalogServiceProxy.service;

import com.example.ProductCatalogServiceProxy.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();

    Product getProduct(Long productId);

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    Product deleteProduct(Long id);
}
