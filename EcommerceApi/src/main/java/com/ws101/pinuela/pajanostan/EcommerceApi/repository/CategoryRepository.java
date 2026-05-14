package com.ws101.pinuela.pajanostan.EcommerceApi.repository;

import com.ws101.pinuela.pajanostan.EcommerceApi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Category entity.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}