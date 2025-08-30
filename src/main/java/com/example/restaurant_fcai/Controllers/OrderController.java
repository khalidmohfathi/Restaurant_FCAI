package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.Entities.*;
import com.example.restaurant_fcai.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getOrders(@AuthenticationPrincipal Customer customer) {
        try {
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
            }

            List<Order> orders = orderService.getCustomerOrders(customer.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders.stream().map(order -> {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", order.getId());
                orderMap.put("total_price", order.getTotalPrice());
                orderMap.put("customer_id", order.getCustomer().getId());
                orderMap.put("table_id", order.getCustomerTable() != null ? order.getCustomerTable().getId() : null);
                
                // Get current status (latest status)
                List<OrderStatus> statuses = order.getOrderStatus();
                String currentStatus = statuses.isEmpty() ? "UNKNOWN" : statuses.get(statuses.size() - 1).getStatus();
                orderMap.put("status", currentStatus);
                
                // Add cart items
                if (order.getCart() != null && order.getCart().getCartItem() != null) {
                    orderMap.put("items", order.getCart().getCartItem().stream().map(item -> {
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("product_id", item.getProduct().getId());
                        itemMap.put("product_name", item.getProduct().getName());
                        itemMap.put("quantity", item.getQuantity());
                        itemMap.put("price", item.getProduct().getPrice());
                        return itemMap;
                    }).toList());
                }
                
                return orderMap;
            }).toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve orders: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(
            @AuthenticationPrincipal Customer customer,
            @RequestBody Map<String, Object> request) {
        try {
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
            }

            Long tableId = request.get("table_id") != null ? Long.valueOf(request.get("table_id").toString()) : null;
            Double totalPrice = request.get("total_price") != null ? Double.valueOf(request.get("total_price").toString()) : null;

            Order order = orderService.placeOrder(customer.getId(), tableId, totalPrice);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Order placed successfully");
            response.put("order", Map.of(
                    "id", order.getId(),
                    "total_price", order.getTotalPrice(),
                    "customer_id", order.getCustomer().getId(),
                    "table_id", order.getCustomerTable() != null ? order.getCustomerTable().getId() : null,
                    "status", "PENDING"
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to create order: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(
            @AuthenticationPrincipal Customer customer,
            @PathVariable Long id) {
        try {
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
            }

            Order order = orderService.getOrderById(id);
            if (order == null) {
                return ResponseEntity.notFound().build();
            }

            // Check if order belongs to customer
            if (!order.getCustomer().getId().equals(customer.getId())) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("id", order.getId());
            response.put("total_price", order.getTotalPrice());
            response.put("customer_id", order.getCustomer().getId());
            response.put("table_id", order.getCustomerTable() != null ? order.getCustomerTable().getId() : null);
            
            // Get current status and history
            List<OrderStatus> statuses = order.getOrderStatus();
            String currentStatus = statuses.isEmpty() ? "UNKNOWN" : statuses.get(statuses.size() - 1).getStatus();
            response.put("status", currentStatus);
            response.put("status_history", statuses.stream().map(status -> Map.of(
                    "status", status.getStatus(),
                    "created_at", status.getCreatedAt()
            )).toList());
            
            // Add cart items
            if (order.getCart() != null && order.getCart().getCartItem() != null) {
                response.put("items", order.getCart().getCartItem().stream().map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("product_id", item.getProduct().getId());
                    itemMap.put("product_name", item.getProduct().getName());
                    itemMap.put("quantity", item.getQuantity());
                    itemMap.put("price", item.getProduct().getPrice());
                    itemMap.put("subtotal", item.getProduct().getPrice() * item.getQuantity());
                    return itemMap;
                }).toList());
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve order: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateOrder(
            @AuthenticationPrincipal Customer customer,
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
            }

            Order order = orderService.getOrderById(id);
            if (order == null) {
                return ResponseEntity.notFound().build();
            }

            // Check if order belongs to customer
            if (!order.getCustomer().getId().equals(customer.getId())) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }

            String newStatus = request.get("status").toString();
            String action = request.get("action") != null ? request.get("action").toString() : null;

            if ("cancel".equals(action)) {
                boolean cancelled = orderService.cancelOrder(id, customer.getId());
                if (!cancelled) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Order cannot be cancelled"));
                }
                newStatus = "CANCELLED";
            } else if (newStatus != null) {
                order = orderService.updateOrderStatus(id, newStatus);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Order updated successfully");
            response.put("order", Map.of(
                    "id", order.getId(),
                    "status", newStatus != null ? newStatus : "UPDATED",
                    "total_price", order.getTotalPrice()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to update order: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(
            @AuthenticationPrincipal Customer customer,
            @PathVariable Long id) {
        try {
            if (customer == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
            }

            Order order = orderService.getOrderById(id);
            if (order == null) {
                return ResponseEntity.notFound().build();
            }

            // Check if order belongs to customer
            if (!order.getCustomer().getId().equals(customer.getId())) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }

            orderService.deleteOrder(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Order deleted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to delete order: " + e.getMessage()));
        }
    }
}