package com.example.ProductCatalogServiceProxy.service;

import com.example.ProductCatalogServiceProxy.dto.ProductDTO;
import com.example.ProductCatalogServiceProxy.model.Category;
import com.example.ProductCatalogServiceProxy.model.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService {
    RestTemplateBuilder restTemplateBuilder;

    ProductService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }
    // issue using List<> while calling restTemplate.getForEntity
//    @Override
//    public List<Product> getProducts() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        // TRYING to get list of ProductDTO objects
//        // from FakeStore API response // But this line is WRONG and does NOT compile: // List<ProductDTO>.class
//        // Reason: // Java Generics use Type Erasure
//        // At runtime: // List<ProductDTO>
//        //            // becomes simply:// List
//        // So JVM does NOT know what ProductDTO is here,
//        // There is NO runtime class called: // List<ProductDTO>.class
//        // That is why Spring cannot deserialize JSON into it
//        // Correct approach: // Use ProductDTO[].class instead
//        // Arrays preserve runtime type information
//        // Correct: ProductDTO[].class
//        // Wrong: // List<ProductDTO>.class
//
//        List<ProductDTO> productDTOs = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", List<ProductDTO>.class).getBody();
//        return new ArrayList<>();
//    }
    // issue is solved by passing as array[]
    @Override
    public List<Product> getProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ProductDTO[] productDTOs = restTemplate.getForEntity("https://fakestoreapi.com/products", ProductDTO[].class).getBody();
        List<Product> products = new ArrayList<>();
        for(ProductDTO productDTO : productDTOs) {
            products.add(getProduct(productDTO));
        }
        return products;
    }
    // get 1 product from api
    @Override
    public Product getProduct(Long productId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ProductDTO productDTO = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", ProductDTO.class, productId).getBody();
        return getProduct(productDTO);
    }
    //create product using api
    @Override
    public Product createProduct(ProductDTO productDTO) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<ProductDTO> responseEntity = restTemplate.postForEntity(
                "https://fakestoreapi.com/products",
                productDTO,
                ProductDTO.class);
        return getProduct(responseEntity.getBody());
    }
    @Override
    public String updateProduct(ProductDTO productDTO) {
        return  null;
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

        return product;
    }
}
