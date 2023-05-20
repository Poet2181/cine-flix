package com.purdueglobal.cineflix;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomerId(Long customerId);

    @Query("SELECT DISTINCT c FROM CartItem c JOIN FETCH c.product WHERE c.order.id = :orderId")
    List<CartItem> findByOrderId(@Param("orderId") Long orderId);
    
}