package com.example.restaurant_fcai.Repos;

import com.example.restaurant_fcai.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}
