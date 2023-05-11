package com.purdueglobal.cineflix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EcommerceController {

    //private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public EcommerceController(
        //OrderRepository orderRepository,
        CartItemRepository cartItemRepository,
        CustomerRepository customerRepository
        ) {
        //this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
    }

    // // Repository fields and constructor initialization as shown in the previous examples

    // // Customer self-service functionality for order status and cancellation
    // @GetMapping("/orders/{orderId}")
    // public ResponseEntity<Order> getOrderStatus(@PathVariable Long orderId) {
    //     Optional<Order> order = orderRepository.findById(orderId);
    //     if (order.isPresent()) {
    //         return ResponseEntity.ok(order.get());
    //     } else {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    //     }
    // }

    // @PutMapping("/orders/{orderId}/cancel")
    // public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
    //     Optional<Order> order = orderRepository.findById(orderId);
    //     if (order.isPresent()) {
    //         Order updatedOrder = order.get();
    //         if ("Not Shipped".equalsIgnoreCase(updatedOrder.getStatus())) {
    //             updatedOrder.setStatus("Cancelled");
    //             orderRepository.save(updatedOrder);
    //             return ResponseEntity.ok(updatedOrder);
    //         } else {
    //             return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    //         }
    //     } else {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    //     }
    // }

    // Add items to cart
    @PostMapping("/cart/{customerId}")
    public ResponseEntity<List<CartItem>> addToCart(@PathVariable Long customerId,
            @RequestBody List<CartItem> cartItems) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            Customer foundCustomer = customer.get();
            for (CartItem cartItem : cartItems) {
                cartItem.setCustomer(foundCustomer);
            }
            List<CartItem> savedCartItems = cartItemRepository.saveAll(cartItems);
            return ResponseEntity.ok(savedCartItems);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @GetMapping("/cart/{customerId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable Long customerId) {
        List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);
        return ResponseEntity.ok(cartItems);
    }

}