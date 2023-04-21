package com.purdueglobal.cineflix;

import jakarta.persistence.*;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Customer customer;
    private Integer quantity;
    // Other cart item fields and getters/setters
}