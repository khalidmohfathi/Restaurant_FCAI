package com.example.restaurant_fcai.Repos;

import com.example.restaurant_fcai.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
