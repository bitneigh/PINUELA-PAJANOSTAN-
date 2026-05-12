package com.ws101.pinuela.pajanostan.EcommerceApi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a customer order entity in the e-commerce system.
 * This class demonstrates a One-to-Many relationship where one order
 * can contain multiple order items.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date and time when the order was placed.
     */
    private LocalDateTime orderDate;

    /**
     * Relationship Mapping: One Order has many OrderItems.
     * Requirement: Use CascadeType.ALL to ensure items are saved with the order,
     * and FetchType. LAZY for optimized data loading.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;
}