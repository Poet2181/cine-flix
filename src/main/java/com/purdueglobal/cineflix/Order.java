package com.purdueglobal.cineflix;

import jakarta.persistence.*;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    @ManyToOne
    private Customer customer;
    // Other order fields and getters/setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}