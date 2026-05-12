package com.ws101.pinuela.pajanostan.EcommerceApi.repository;

import com.ws101.pinuela.pajanostan.EcommerceApi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 3.2.1: Method Naming
    List<Product> findByCategoryNameIgnoreCase(String name);

    // 3.2.2: @Query with JPQL 
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    List<Product> findProductsByPriceRange(@Param("min") Double min, @Param("max") Double max);
}