package com.example.ProductCatalogServiceProxy.controller;

import com.example.ProductCatalogServiceProxy.model.Product;
import com.example.ProductCatalogServiceProxy.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductControllerTest { // Unit Testing So this test checks:
//“Does the controller method logic work correctly?”
//    It does NOT test:
        //    URL mapping
        //    JSON conversion
        //    HTTP request handling
        //    DispatcherServlet
        //    Request body parsing
//    It only tests Java method logic.

    @Autowired
    private ProductController productController;
//    @Autowired
    // mock used here in test doubles
    @MockitoBean // using this bcz productService interacts with db, i.e. we use mocking
    private ProductService productService;

    @Test
    public void Test_GetProduct_ReturnProduct() {
        //Arrange
        Product product = new Product();
        product.setPrice(1000D);
        product.setTitle("Iphone15");

        //stubb used here in doubles
        when(productService.getProduct(any(Long.class))).thenReturn(product);
//        when(productService.getProduct(any(Long.class))).thenReturn(new Product());


        //Act=> mock is actually used here, Whenever getProduct() is called on the mocked productService, return this fake product object.
        ResponseEntity<Product> productResponseEntity = productController.getProduct(1L);

        //Assert
        assertNotNull(productResponseEntity);
        assertEquals(1000D,productResponseEntity.getBody().getPrice());
        assertEquals("Iphone15",productResponseEntity.getBody().getTitle());
        verify(productService, times(1)).getProduct(1L);
    }

    @Test
    @DisplayName("dependency threw an exception ")
    public void Test_GetProduct_InternalDependencyThrowsException() {
        //Arrange
        when(productService.getProduct(any(Long.class))).thenThrow(new RuntimeException("Something went very wrong"));

        //Act and Assert
        assertThrows(RuntimeException.class, ()-> productController.getProduct(1L));
    }

    @Test
    @DisplayName("wrong id 0 lead to an exception")
    public void Test_GetProductWithInvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> productController.getProduct(0L));
    }

    // we can also add test cases for headers, but we will do that in the integration test, bcz here we are only testing the controller and not the http response

    //Test Get All Products
    @Test
    @DisplayName("Get all products successfully")
    public void Test_GetAllProducts_ReturnListOfProducts() {
        //Arrange
        Product p1 = new Product();
        p1.setTitle("IPhone 11");
        Product p2 = new Product();
        p2.setTitle("MacBook 14");
        when(productService.getProducts()).thenReturn(List.of(p1, p2));

        //Act
        List<Product> products = productController.getProducts();

        //Assert
        assertNotNull(products);
        assertEquals(2, products.size());

        verify(productService, times(1)).getProducts();
    }

}