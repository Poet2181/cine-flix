package com.purdueglobal.cineflix.repository;

import com.purdueglobal.cineflix.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}