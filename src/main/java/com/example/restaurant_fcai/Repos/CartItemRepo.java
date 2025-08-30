package com.example.restaurant_fcai.Repos;

import com.example.restaurant_fcai.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
}
