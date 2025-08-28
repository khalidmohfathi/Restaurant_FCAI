package com.example.restaurant_fcai.Repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restaurant_fcai.Entities.Category;

public interface  CategoryRepo extends JpaRepository<Category, Long> {

}
