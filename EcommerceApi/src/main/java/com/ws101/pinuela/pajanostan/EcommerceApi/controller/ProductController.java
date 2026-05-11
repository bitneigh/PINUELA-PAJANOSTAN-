package com.ws101.pinuela.pajanostan.EcommerceApi.controller;

import com.ws101.pinuela.pajanostan.EcommerceApi.model.Product;
import com.ws101.pinuela.pajanostan.EcommerceApi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class that handles all HTTP requests related to product management.
 * Provides endpoints for CRUD operations and filtering.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Retrieves all products from the system.
     * * @return A list of all products with a 200 OK status.
     */
    // GET /api/v1/products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    /**
     * Retrieves a specific product by its unique ID.
     * * @param id The unique identifier of the product.
     * @return The product details if found (200 OK), or a 404 Not Found status.
     */
    // GET /api/v1/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Filters products based on a specific criteria like category.
     * * @param filterType The type of filter to apply (e.g., "category").
     * @param filterValue The value to filter by (e.g., "Electronics").
     * @return A list of filtered products with 200 OK status.
     */
    // GET /api/v1/products/filter?filterType=category&filterValue=Electronics
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam String filterType,
            @RequestParam String filterValue) {

        if ("category".equalsIgnoreCase(filterType)) {
            return new ResponseEntity<>(productService.filterByCategory(filterValue), HttpStatus.OK);
        }
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    /**
     * Creates a new product entry in the system.
     * * @param product The product object to be created.
     * @return The created product with a 201 Created status.
     * throws org.springframework.web.bind.MethodArgumentNotValidException if validation fails.
     */
    // POST /api/v1/products
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Updates an existing product's information.
     * * @param id The ID of the product to update.
     * @param product The updated product data.
     * @return The updated product (200 OK) or 404 Not Found if the ID does not exist.
     * throws org.springframework.web.bind.MethodArgumentNotValidException if the new data is invalid.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct != null) {
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Deletes a product from the system using its ID.
     * * @param id The unique identifier of the product to be deleted.
     * @return A 204 No Content status if successful, or 404 Not Found.
     */
    // DELETE /api/v1/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}