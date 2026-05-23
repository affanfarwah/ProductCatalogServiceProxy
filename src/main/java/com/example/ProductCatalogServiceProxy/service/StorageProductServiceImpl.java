package com.example.ProductCatalogServiceProxy.service;

import com.example.ProductCatalogServiceProxy.model.Product;
import com.example.ProductCatalogServiceProxy.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StorageProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public StorageProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        Product resultProduct = productRepository.save(product);
        return resultProduct;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id).orElse(null);

        if (existingProduct == null) {
            return null;
        }

        existingProduct.setTitle(product.getTitle());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setCategory(product.getCategory());

        return productRepository.save(existingProduct);
    }
    @Override
    public Product deleteProduct(Long id) throws IllegalArgumentException {
        return null;
    }
}