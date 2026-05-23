package com.example.ProductCatalogServiceProxy.repository;

import com.example.ProductCatalogServiceProxy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product product);
    Product findProductById(Long id);

    List<Product> findProductByPriceBetween(Double low, Double high);

    List<Product> findAllByOrderByIdDesc();

//    List<Product> findAllByIsSpecialTrue();

    @Query("select p.title from Product p where p.id = ?1") //? represents parameters-> ?1-Long, ?2-String
    String getProductTitleFromId(Long id);
//    String getProductTitleFromId(Long id, String s);

    @Query("select c.name from Product p join category c on p.category.id = c.id where p.id = :product_id")
    String getCategoryNameFromProductId(@Param("product_id") Long id);
}
