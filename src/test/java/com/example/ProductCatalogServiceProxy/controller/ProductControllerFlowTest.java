package com.example.ProductCatalogServiceProxy.controller;

import com.example.ProductCatalogServiceProxy.dto.ProductDTO;
import com.example.ProductCatalogServiceProxy.model.Product;
import com.example.ProductCatalogServiceProxy.service.ProductService;
import com.example.ProductCatalogServiceProxy.stubs.ProductServiceStub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProductControllerFlowTest {
    @Autowired
    private ProductController productController;
    @Autowired
    private ProductService productService;

    // Use stub instead of DB service
//    ProductService productService =
//            new ProductServiceStub();
//
//    // Inject stub into controller
//    ProductController productController =
//            new ProductController(productService);

    @Test
    public void Test_CreateAndFetchAndUpdate_RunsSuccessfully() {
        //Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setTitle("ABC");
        productDTO.setDescription("XYZ");

        //Act
        productController.createProduct(productDTO); // create

        ResponseEntity<Product> productResponseEntity = productController.getProduct(1L);
        productDTO.setTitle("DEF");
        productDTO.setPrice(1000.0);
        productController.updateProduct(1L, productDTO);
        ResponseEntity<Product> updatedResponseEntity = productController.getProduct(1L);

        //Assert
        assertEquals("ABC", productResponseEntity.getBody().getTitle());
        assertEquals("XYZ", productResponseEntity.getBody().getDescription());
        assertEquals("DEF", updatedResponseEntity.getBody().getTitle());
        assertEquals(1000.0, updatedResponseEntity.getBody().getPrice());
    }
}
