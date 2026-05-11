package com.example.ProductCatalogServiceProxy.controller;

import com.example.ProductCatalogServiceProxy.dto.ProductDTO;
import org.springframework.web.bind.annotation.*;

//@RequestMapping("/products")
@RestController
public class ProductController {
    @GetMapping("/products")
    public String getProducts() {
        return "Returning list of all products";
    }
    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable("id") String productId) {
        return "Returning product with id "+ productId;
    }
    @PostMapping("/products")
    public String createProduct(@RequestBody ProductDTO productDTO) {
        return "Creating Product -- " + productDTO;
    }
    @PatchMapping("/products")
    public String updateProduct(@RequestBody ProductDTO productDTO) {
        return "Updating Product -- " + productDTO;
    }
}
