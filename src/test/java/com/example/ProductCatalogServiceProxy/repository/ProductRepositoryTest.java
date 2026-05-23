package com.example.ProductCatalogServiceProxy.repository;

import com.example.ProductCatalogServiceProxy.model.Category;
import com.example.ProductCatalogServiceProxy.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProductRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    void demonstrateLoading() {
        Category category = categoryRepository.findById(2L).get(); //lazy bydefault
        System.out.println(category.getName());
//        List<Product> products = category.getProducts();
//        for(Product product : products) {
//            System.out.println(product.getId());
//        }
    }
    @Test
    @Transactional
    @Rollback(value = false)
    void demonstrateNplus1Problem() {
        // Step 1: Fetch all categories
        List<Category> categories = categoryRepository.findAll();   // Query 1
        // Step 2: Loop through each category
        for(Category category : categories) {
            // Step 3: For every category, try to get its products
            List<Product> products = category.getProducts();   // ← This is the PROBLEM line
            // Step 4: Just print first product's price (for testing)
            if(!products.isEmpty()) {
                System.out.println(products.get(0).getPrice());
            }
        }
    }
//      Lazy + Select => runs n+1 queries, n= no of categories
//      Lazy + subSelect => runs only 2 queries, one, Fetch all Categories, other one Fetch ALL Products for ALL Categories in ONE query
//                            SELECT * FROM product p
//                            WHERE p.category_id IN (
//                                SELECT c.id FROM category c        -- Subquery
//                            );

    /*
    * @BatchSize
@BatchSize(size = 10) is a Hibernate optimization for LAZY collections.
Instead of firing 1 query per entity (N+1), it fetches associated data in small batches.
If you have 25 categories and batch size is 10, It will fire only 3 queries for products (10 + 10 + 5)
Total Queries = 1 + 3 = 4 queries (instead of 26)
* */

    @Test
    @Transactional
    @Rollback(value = false)
    void demonstrateHowJPAGeneratesQueries() {
        //Product product = productRepo.findProductById(2L);
        //List<Product> products = productRepo.findProductByPriceBetween(250D,2500D);
        //List<Product> products = productRepo.findAllByOrderByIdDesc();
//        List<Product> products = productRepository.findAllByIsSpecialTrue();

//        String name = productRepository.getProductTitleFromId(2L);
        String name = productRepository.getCategoryNameFromProductId(2L);
        System.out.println("name: " + name);
        System.out.println("debug");
    }

}