package com.example.ProductCatalogServiceProxy.service;

import com.example.ProductCatalogServiceProxy.clients.fakeStore.dtos.FakeStoreProductDTO;
import com.example.ProductCatalogServiceProxy.dto.ProductDTO;
import com.example.ProductCatalogServiceProxy.model.Product;

import java.util.List;

public interface IProductService {
    List<Product> getProducts();

    Product getProduct(Long productId);

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);
}
