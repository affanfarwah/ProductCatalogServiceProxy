package com.example.ProductCatalogServiceProxy.controller;

import com.example.ProductCatalogServiceProxy.dto.ProductDTO;
import com.example.ProductCatalogServiceProxy.model.Category;
import com.example.ProductCatalogServiceProxy.model.Product;
import com.example.ProductCatalogServiceProxy.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/products")
@RestController
public class ProductController {
    ProductService productService;
    ProductController(ProductService productService) {
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
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            throw exception;
        }
    }
    @PostMapping("")
    public Product createProduct(@RequestBody ProductDTO productDTO) {
        Product product = getProduct(productDTO);
        return productService.createProduct(product);
    }
    @PatchMapping("{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        Product product = getProduct(productDTO);
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("{id}")
    public Product deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    //helper method for converting productDTO into Product
    private Product getProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setDescription((productDTO.getDescription()));
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImage());
        Category category = new Category();
        category.setName(productDTO.getCategory());
        product.setCategory(category);
        product.setId(productDTO.getId());
        product.setStatus(productDTO.getStatus());
        return product;
    }
    // suppose you went to the hotel, Order a chicken biryani to the waiter,
    // waiter come back saying the chicken is out of stock but sir you can order this and this
    // similarly, when client orders something via a controller and it is not present or something went wrong
    // controller needs to send a message this is the issue, here's how you can do this and this
//    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
//    private ResponseEntity<String> handleException() {
//        return new ResponseEntity<String>("Kuch toh gadbad hai", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
    // this only works if an exception occur in this file, to work everywhere use RestControllerAdvice
}
