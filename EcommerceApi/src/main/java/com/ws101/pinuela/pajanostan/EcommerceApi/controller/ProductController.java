package com.ws101.pinuela.pajanostan.EcommerceApi.controller;

import com.ws101.pinuela.pajanostan.EcommerceApi.dto.ProductRequest;
import com.ws101.pinuela.pajanostan.EcommerceApi.model.Product;
import com.ws101.pinuela.pajanostan.EcommerceApi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Requirement: GET all products from the database.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Requirement: GET a single product by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Task 4: POST a new product using DTO and Validation.
     * We use @Valid to trigger the constraints in ProductRequest.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest productDto) {
        // Mapping DTO to Entity
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());

        return ResponseEntity.ok(productService.createProduct(product));
    }

    /**
     * Requirement: PUT (Update) an existing product.
     * Added @Valid here as well to ensure updates are also validated.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());

        Product updated = productService.updateProduct(id, product);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * Requirement: DELETE a product by ID.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // --- Task 3 Custom Query Endpoints ---

    /**
     * Requirement 3.2.1: Filter by category name.
     */
    @GetMapping("/filter/category")
    public List<Product> filterByCategory(@RequestParam String name) {
        return productService.filterByCategory(name);
    }

    /**
     * Requirement 3.2.2: Filter by price range using JPQL.
     */
    @GetMapping("/filter/price")
    public List<Product> filterByPrice(@RequestParam Double min, @RequestParam Double max) {
        return productService.getProductsByPrice(min, max);
    }
}