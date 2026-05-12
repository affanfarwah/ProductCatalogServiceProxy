package com.example.ProductCatalogServiceProxy.controller;

import com.example.ProductCatalogServiceProxy.dto.ProductDTO;
import com.example.ProductCatalogServiceProxy.model.Product;
import com.example.ProductCatalogServiceProxy.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/products")
@RestController
public class ProductController {
    IProductService productService;
    ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public List<Product> getProducts() {
        return productService.getProducts();
    }
//    @GetMapping("{id}")
//    public Product getProduct(@PathVariable("id") Long productId) {
//        return productService.getProduct(productId);
//    }
    // we can customize http response codes, like 200, 400, 404 etc
//    @GetMapping("{id}")
//    public ResponseEntity<Product> getProduct(@PathVariable("id") Long productId) {
//        Product product = productService.getProduct(productId);
//        return new ResponseEntity<>(product, HttpStatus.BAD_REQUEST);
//    }

    // we can also add exception if something goes wrong
//    @GetMapping("{id}")
//    public ResponseEntity<Product> getProduct(@PathVariable("id") Long productId) {
//        try {
//            if(productId < 1) {
//                throw new IllegalArgumentException("ProductId is incorrect");
//            }
//            Product product = productService.getProduct(productId);
//            return new ResponseEntity<>(product, HttpStatus.BAD_REQUEST);
//        } catch (Exception exception) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    // handling headers
    @GetMapping("{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long productId) {
        try {
            if(productId < 1) {
                throw new IllegalArgumentException("ProductId is incorrect");
            }
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("called-by", "pagal");
            Product product = productService.getProduct(productId);
            return new ResponseEntity<>(product, headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("")
    public Product createProduct(@RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }
    @PatchMapping("")
    public String updateProduct(@RequestBody ProductDTO productDTO) {
        return "Updating Product -- " + productDTO;
    }
}
