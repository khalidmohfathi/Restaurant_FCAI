package com.example.restaurant_fcai.Repos;

import com.example.restaurant_fcai.Entities.Cart;
import com.example.restaurant_fcai.Entities.CartItem;
import com.example.restaurant_fcai.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
    List<CartItem> findByCartId(Long cartId);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    void deleteByCartIdAndProductId(Long cartId, Long productId);
}