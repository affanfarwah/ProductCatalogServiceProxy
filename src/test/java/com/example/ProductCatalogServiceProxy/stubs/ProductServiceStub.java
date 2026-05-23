package com.example.ProductCatalogServiceProxy.stubs;

import com.example.ProductCatalogServiceProxy.model.Product;
import com.example.ProductCatalogServiceProxy.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//for testing if controller + service flow works.
//@Service
public class ProductServiceStub implements ProductService { /*fake implementation of ProductService.
Instead of using:
    real database
    real repository
    real external systems
it stores data in memory(Fake database inside RAM) using: Map<Long, Product> products;*/
    Map<Long,Product> products;

    public Product getProductDetails(Long userId, Long productId) {
        return null;
    }

    public ProductServiceStub() {
        products = new HashMap<Long,Product>();
    }

    @Override
    public List<Product> getProducts() {
        return List.of();
    }

    @Override
    public Product getProduct(Long productId) {
        return products.get(productId);
    }

    @Override
    public Product createProduct(Product product) {
        products.put(product.getId(),product);
        return products.get(product.getId());
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        products.put(id,product);
        return products.get(id);
    }

    @Override
    public Product deleteProduct(Long id) {
        return null;
    }
}