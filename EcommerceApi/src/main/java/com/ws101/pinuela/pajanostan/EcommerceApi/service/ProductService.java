package com.ws101.pinuela.pajanostan.EcommerceApi.service;

import com.ws101.pinuela.pajanostan.EcommerceApi.model.Product;
import com.ws101.pinuela.pajanostan.EcommerceApi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class that manages business logic for Product operations.
 * Refactored to use Spring Data JPA Repository instead of an ArrayList.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository; // Injected repository to replace manual ArrayList

    /**
     * Retrieves all products from the database.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll(); // Simplified using JPA findAll()
    }

    /**
     * Finds a product by its unique database ID.
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));
    }

    /**
     * Saves a new product or updates an existing one in the database.
     */
    public Product createProduct(Product product) {
        return productRepository.save(product); // Manual ID counter removed; JPA handles auto-increment
    }

    /**
     * Deletes a product from the database by ID.
     */
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Task 3.2.1: Method Naming query to filter by category name.
     */
    public List<Product> filterByCategory(String category) {
        return productRepository.findByCategoryNameIgnoreCase(category); // Uses the method naming convention
    }

    /**
     * Task 3.2.2: Uses JPQL @Query to find products within a price range.
     */
    public List<Product> getProductsByPrice(Double min, Double max) {
        return productRepository.findProductsByPriceRange(min, max); // Uses the custom @Query defined in repository
    }

    /**
     * Updates an existing product's details.
     */
    public Product updateProduct(Long id, Product updatedProduct) {
        if (productRepository.existsById(id)) {
            updatedProduct.setId(id);
            return productRepository.save(updatedProduct);
        }
        return null;
    }
}