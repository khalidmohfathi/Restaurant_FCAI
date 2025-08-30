package com.example.restaurant_fcai.Repos;

import com.example.restaurant_fcai.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
}
