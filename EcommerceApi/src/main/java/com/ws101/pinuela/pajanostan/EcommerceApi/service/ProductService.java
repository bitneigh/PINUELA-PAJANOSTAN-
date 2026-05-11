package com.ws101.pinuela.pajanostan.EcommerceApi.service;

import com.ws101.pinuela.pajanostan.EcommerceApi.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class that manages the business logic for Product operations.
 * Uses an in-memory list as a temporary data store.
 */
@Service
public class ProductService {
    // In-memory list to store products
    private List<Product> products = new ArrayList<>();
    private Long nextId = 1L; // Simple counter for unique IDs

    /**
     * Constructor that initializes the service with 10 sample product entries.
     */
    public ProductService() {
        // Initialize with sample data (at least 10 products)
        for (int i = 1; i <= 10; i++) {
            products.add(new Product((long) i, "Product " + i, "Description for product " + i,
                    100.0 * i, "Category " + (i % 3), 10 + i, "http://image.url/" + i));
            nextId++;
        }
    }

    /**
     * Retrieves the complete list of products currently in the system.
     * * @return A list containing all products.
     */
    // Method to retrieve all products
    public List<Product> getAllProducts() {
        return products;
    }

    /**
     * Searches for a specific product by its ID using Java Streams.
     * * @param id The unique ID of the product to search for.
     * @return The product object if found.
     * @throws RuntimeException If no product matches the provided ID.
     */
    // Method to find a product by ID
    public Product getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));
    }

    /**
     * Adds a new product to the list and assigns it a unique generated ID.
     * * @param product The product object to be added.
     * @return The saved product with its assigned ID.
     */
    // Method to create a new product
    public Product createProduct(Product product) {
        product.setId(nextId++); // Assign unique ID
        products.add(product);
        return product;
    }

    /**
     * Removes a product from the list based on its ID.
     * * @param id The ID of the product to be removed.
     * @return True if a product was removed, false otherwise.
     */
    // Method to delete a product
    public boolean deleteProduct(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    /**
     * Filters the product list by category (case-insensitive).
     * * @param category The name of the category to filter by.
     * @return A filtered list of products belonging to the specified category.
     */
    // Filter by category
    public List<Product> filterByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing product's details in the list.
     * * @param id The ID of the product to be updated.
     * @param updatedProduct The new product data to apply.
     * @return The updated product object, or null if the ID was not found.
     */
    public Product updateProduct(Long id, Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                updatedProduct.setId(id);
                products.set(i, updatedProduct);
                return updatedProduct;
            }
        }
        return null;
    }
}