package com.ws101.pinuela.pajanostan.EcommerceApi.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an individual item within an order.
 * Links a specific Product to an Order with a defined quantity.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    /**
     * Relationship Mapping: Many OrderItems can refer to one Product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Relationship Mapping: Many OrderItems belong to one Order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}