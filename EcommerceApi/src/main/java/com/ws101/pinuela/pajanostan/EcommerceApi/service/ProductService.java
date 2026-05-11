package com.ws101.pinuela.pajanostan.EcommerceApi.service;

import com.ws101.pinuela.pajanostan.EcommerceApi.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    // In-memory list to store products
    private List<Product> products = new ArrayList<>();
    private Long nextId = 1L; // Simple counter for unique IDs

    public ProductService() {
        // Initialize with sample data (at least 10 products)
        for (int i = 1; i <= 10; i++) {
            products.add(new Product((long) i, "Product " + i, "Description for product " + i,
                    100.0 * i, "Category " + (i % 3), 10 + i, "http://image.url/" + i));
            nextId++;
        }
    }

    // Method to retrieve all products
    public List<Product> getAllProducts() {
        return products;
    }

    // Method to find a product by ID
    public Product getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Method to create a new product
    public Product createProduct(Product product) {
        product.setId(nextId++); // Assign unique ID
        products.add(product);
        return product;
    }

    // Method to delete a product
    public boolean deleteProduct(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    // Filter by category
    public List<Product> filterByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}