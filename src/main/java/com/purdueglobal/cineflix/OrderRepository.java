package com.purdueglobal.cineflix;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.cartItems WHERE o.id = :orderId")
    Optional<Order> findByIdWithCartItems(@Param("orderId") Long orderId);

    @Query("SELECT SUM(c.quantity) FROM CartItem c WHERE c.order.id = :orderId")
    int getTotalItemsByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT SUM(c.product.price * c.quantity) FROM CartItem c WHERE c.order.id = :orderId")
    BigDecimal getTotalPriceByOrderId(@Param("orderId") Long orderId);

    List<Order> findByCustomerId(Long customerId);
}
