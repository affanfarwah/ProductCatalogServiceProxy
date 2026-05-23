package com.example.ProductCatalogServiceProxy.repository;

import com.example.ProductCatalogServiceProxy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
