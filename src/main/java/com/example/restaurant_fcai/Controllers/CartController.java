package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.Entities.*;
import com.example.restaurant_fcai.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCart(@AuthenticationPrincipal Customer customer) {
        try {
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
            }

            Cart cart = cartService.getOrCreateCart(customer);
            List<CartItem> cartItems = cartService.getCartItems(customer.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("cart_id", cart.getId());
            response.put("customer_id", customer.getId());
            response.put("total_price", cart.getTotalPrice());
            response.put("items", cartItems.stream().map(item -> {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("product_id", item.getProduct().getId());
                itemMap.put("product_name", item.getProduct().getName());
                itemMap.put("price", item.getProduct().getPrice());
                itemMap.put("quantity", item.getQuantity());
                itemMap.put("subtotal", item.getProduct().getPrice() * item.getQuantity());
                return itemMap;
            }).toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve cart: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addToCart(
            @AuthenticationPrincipal Customer customer,
            @RequestBody Map<String, Object> request) {
        try {
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
            }

            Long productId = Long.valueOf(request.get("product_id").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());

            if (quantity <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Quantity must be greater than 0"));
            }

            CartItem cartItem = cartService.addProductToCart(customer.getId(), productId, quantity);
            Cart cart = cartService.getOrCreateCart(customer);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product added to cart successfully");
            response.put("cart_item", Map.of(
                    "product_id", cartItem.getProduct().getId(),
                    "product_name", cartItem.getProduct().getName(),
                    "quantity", cartItem.getQuantity(),
                    "price", cartItem.getProduct().getPrice()
            ));
            response.put("cart_total", cart.getTotalPrice());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to add product to cart: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @AuthenticationPrincipal Customer customer,
            @PathVariable Long product_id) {
        try {
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
            }

            boolean removed = cartService.removeProductFromCart(customer.getId(), product_id);
            
            if (!removed) {
                return ResponseEntity.notFound().build();
            }

            Cart cart = cartService.getOrCreateCart(customer);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product removed from cart successfully");
            response.put("cart_total", cart.getTotalPrice());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to remove product from cart: " + e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> clearCart(@AuthenticationPrincipal Customer customer) {
        try {
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
            }

            cartService.clearCart(customer.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cart cleared successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to clear cart: " + e.getMessage()));
        }
    }
}