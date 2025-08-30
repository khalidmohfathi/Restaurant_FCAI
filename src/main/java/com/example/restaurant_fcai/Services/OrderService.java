package com.example.restaurant_fcai.Services;

import com.example.restaurant_fcai.Entities.*;
import com.example.restaurant_fcai.Repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CartService cartService;

    public List<Order> getCustomerOrders(Long customerId) {
        return orderRepo.findByCustomerIdOrderByIdDesc(customerId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }

    public Order placeOrder(Long customerId, Long tableId, Double totalPrice) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Cart cart = cartService.getCartByCustomerId(customerId);
        if (cart == null || cart.getCartItem().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Create new order
        Order order = new Order();
        order.setCustomer(customer);
        order.setCart(cart);
        order.setTotalPrice(totalPrice != null ? totalPrice : cart.getTotalPrice());
        
        // Set customer table if provided
        if (tableId != null) {
            CustomerTable customerTable = new CustomerTable();
            customerTable.setId(tableId);
            order.setCustomerTable(customerTable);
        }

        order = orderRepo.save(order);

        // Create initial order status
        OrderStatus initialStatus = new OrderStatus();
        initialStatus.setOrder(order);
        initialStatus.setStatus("PENDING");
        initialStatus.setCreatedAt(new Date());
        
        // Add status to order
        order.getOrderStatus().add(initialStatus);
        order = orderRepo.save(order);

        // Clear the cart after placing order
        cartService.clearCart(customerId);

        return order;
    }

    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Create new status entry
        OrderStatus statusUpdate = new OrderStatus();
        statusUpdate.setOrder(order);
        statusUpdate.setStatus(newStatus);
        statusUpdate.setCreatedAt(new Date());
        
        order.getOrderStatus().add(statusUpdate);
        return orderRepo.save(order);
    }

    public List<OrderStatus> getOrderHistory(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return order.getOrderStatus();
    }

    public boolean cancelOrder(Long orderId, Long customerId) {
        Optional<Order> orderOpt = orderRepo.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            
            // Check if order belongs to customer
            if (!order.getCustomer().getId().equals(customerId)) {
                throw new RuntimeException("Unauthorized to cancel this order");
            }
            
            // Check if order can be cancelled (not delivered)
            List<OrderStatus> statuses = order.getOrderStatus();
            String currentStatus = statuses.get(statuses.size() - 1).getStatus();
            
            if ("DELIVERED".equals(currentStatus) || "CANCELLED".equals(currentStatus)) {
                throw new RuntimeException("Order cannot be cancelled");
            }
            
            // Update status to cancelled
            updateOrderStatus(orderId, "CANCELLED");
            return true;
        }
        return false;
    }

    public void deleteOrder(Long orderId) {
        orderRepo.deleteById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
}