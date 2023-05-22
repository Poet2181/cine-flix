package com.purdueglobal.cineflix.repository;

import com.purdueglobal.cineflix.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}