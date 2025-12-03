package com.TechMarket.product_service.repository;

import com.TechMarket.product_service.model.Category;
import com.TechMarket.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByCategoriesContaining(Category category);
    boolean existsByPrimaryCategory(Category category);
}
