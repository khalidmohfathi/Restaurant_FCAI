package com.example.restaurant_fcai.Repos;

import com.example.restaurant_fcai.Entities.Cart;
import com.example.restaurant_fcai.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomer(Customer customer);
    Optional<Cart> findByCustomerId(Long customerId);
}