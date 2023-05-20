package com.purdueglobal.cineflix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EcommerceController {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public EcommerceController(
            OrderRepository orderRepository,
            CartItemRepository cartItemRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/orders/{customerId}")
    public ResponseEntity<Order> placeOrder(@PathVariable Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);
            if (!cartItems.isEmpty()) {
                // Process the order and create an Order object
                Order order = new Order();
                order.setCustomer(customer.get());
                order.setStatus("NotShipped");
                order.setDateCreated(LocalDate.now());

                // Add the cart items to the order
                for (CartItem cartItem : cartItems) {
                    order.addCartItem(cartItem);
                }

                // Save the order in the database
                Order savedOrder = orderRepository.save(order);

                // Clear the cart for the customer
                clearCart(customerId);
                return ResponseEntity.ok(savedOrder);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // // Customer self-service functionality for order status and cancellation
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrderStatus(@PathVariable Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order fetchedOrder = order.get();
            return ResponseEntity.ok(fetchedOrder);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order updatedOrder = order.get();
            if ("NotShipped".equalsIgnoreCase(updatedOrder.getStatus())) {
                updatedOrder.setStatus("Cancelled");
                orderRepository.save(updatedOrder);
                return ResponseEntity.ok(updatedOrder);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Add items to cart
    @PostMapping("/cart/{customerId}")
    public ResponseEntity<List<CartItem>> addToCart(@PathVariable Long customerId,
            @RequestBody List<CartItem> cartItems) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            Customer foundCustomer = customer.get();
            for (CartItem cartItem : cartItems) {
                if (cartItem.getProduct() != null && cartItem.getProduct().getId() == null) {
                    Product savedProduct = productRepository.save(cartItem.getProduct());
                    cartItem.setProduct(savedProduct);
                }
                cartItem.setCustomer(foundCustomer);
            }
            List<CartItem> savedCartItems = cartItemRepository.saveAll(cartItems);
            return ResponseEntity.ok(savedCartItems);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cart/{customerId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long customerId) {
        List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/cart/{customerId}")
    public ResponseEntity<String> clearCart(@PathVariable Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);
            cartItems.forEach(cartItem -> cartItem.setCustomer(null)); // Remove customer association
            cartItemRepository.saveAll(cartItems); // Update the cart items in the database
            cartItemRepository.deleteAll(cartItems); // Delete the cart items
    
            return ResponseEntity.ok("Cart items cleared successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/cart/{customerId}/items/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long customerId, @PathVariable Long cartItemId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
            if (cartItem.isPresent()) {
                CartItem foundCartItem = cartItem.get();
                if (foundCartItem.getCustomer().getId().equals(customerId)) {
                    cartItemRepository.delete(foundCartItem);
                    return ResponseEntity.ok("Cart item removed successfully.");
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}