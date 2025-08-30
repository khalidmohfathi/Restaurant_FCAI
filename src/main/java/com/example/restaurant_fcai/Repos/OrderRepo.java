package com.example.restaurant_fcai.Repos;

import com.example.restaurant_fcai.Entities.Customer;
import com.example.restaurant_fcai.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByCustomerIdOrderByIdDesc(Long customerId);
}