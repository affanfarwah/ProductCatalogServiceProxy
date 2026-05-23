package com.example.ProductCatalogServiceProxy.controller;

import com.example.ProductCatalogServiceProxy.model.Product;
import com.example.ProductCatalogServiceProxy.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat; //assert j dependency
import static org.hamcrest.MatcherAssert.assertThat; // hamcrest dependency
//Start only the Spring MVC infrastructure needed for ProductController

/*Full Internal Flow
MockMvc.perform()
       ↓
Mock Request Created
       ↓
DispatcherServlet
       ↓
Handler Mapping
       ↓
Controller Method
       ↓
Response Generated
       ↓
Assertions Run*/
@WebMvcTest(ProductController.class) //
public class ProductControllerMVCTest { //starts ONLY the Spring MVC layer.Not full application. Not DB. Not repositories.
//    Only Test:
    //    Controller
    //    DispatcherServlet
    //    URL mappings
    //    JSON conversion
    //    HTTP request/response pipeline

    @Autowired
    private ProductController productController;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // mock of productService
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @Test
    @DisplayName("GET /products")
    public void Test_getProducts_ReceiveSuccessfulResponse() throws Exception {
        //Arrange- Prepare everything needed for the test. -create objects -prepare mock data -configure mocks
        List<Product> productList = new ArrayList<>();
        Product p1 = new Product();
        p1.setTitle("IPhone 11");
        Product p2 = new Product();
        p2.setTitle("MacBook 14");
        productList.add(p1);
        productList.add(p2);
        when(productService.getProducts()).thenReturn(productList);
        //Act and assert
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(productList)));

        /*When you write: mockMvc.perform(get("/products"))

        Spring simulates a real HTTP request internally.

        Flow:

            Fake HTTP Request
                   ↓
            DispatcherServlet
                   ↓
            URL Mapping
                   ↓
            Controller
                   ↓
            Response Body → JSON
                   ↓
            Assertions

        So this tests the real MVC flow.*/
    }

    @Test
    @DisplayName("POST /products")
    public void Test_createProduct_ReceiveSuccessfulResponse() throws Exception {
        //arrange
        Product createdProduct = new Product();
        createdProduct.setTitle("Orange");
        createdProduct.setDescription("Orange is a fruit");

        Product expectedProduct = new Product();
        expectedProduct.setId(1000L);
        expectedProduct.setTitle("Orange");
        expectedProduct.setDescription("Orange is a fruit");
        // Mocking ProductService behavior
        // Whenever createProduct() is called with any Product object,
        // return expectedProduct
        when(productService.createProduct(any(Product.class)))
                .thenReturn(expectedProduct);

        //act and assert
        // Perform fake HTTP POST request to /products
        mockMvc.perform(
                        post("/products")
                                // Request content type
                                .contentType(MediaType.APPLICATION_JSON)
                                // Convert Java object -> JSON request body
                                .content(objectMapper.writeValueAsString(createdProduct))
                )
                // Verify HTTP response status is 200 OK
                .andExpect(status().isOk())
                // Verify response JSON matches expectedProduct JSON by // Convert Java object -> JSON request body
                .andExpect(content().string(objectMapper.writeValueAsString(expectedProduct)))
                .andExpect(jsonPath("$.length()").value(10)) // to check internal values of product
                .andExpect(jsonPath("$.title").value("Orange"));
    }

    @Test
    @DisplayName("GET /products/{id}")
    public void Test_getProduct_ReceiveSuccessfulResponse() throws Exception {
        Product product = new Product();
        product.setId(10L);
        product.setTitle("Mango");

        when(productService.getProduct(10L))
                .thenReturn(product);
        mockMvc.perform(get("/products/10"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(product)));
    }

    @Test
    @DisplayName("PATCH /products/{id}")
    public void Test_updateProduct_ReceiveSuccessfulResponse() throws Exception {
        Product product = new Product();
        product.setTitle("Apple"); //updated title


        when(productService.updateProduct(any(Long.class), any(Product.class)))
                .thenReturn(product);

        mockMvc.perform(
                    patch("/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(product))
                ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(product)));
    }
    @Test
    @DisplayName("Invalid id")
    void Test_InvalidId() throws Exception {

        mockMvc.perform(get("/products/0"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void Test_ProductControllerCallsProductServiceWithSameId() {
        //Act
        Long id = 2L;

        //Act
        productController.getProduct(id);

        //Assert
        verify(productService).getProduct(idCaptor.capture());
        assertEquals(id,idCaptor.getValue());
    }
}