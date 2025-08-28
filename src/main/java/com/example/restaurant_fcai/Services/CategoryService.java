package com.example.restaurant_fcai.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant_fcai.DTOs.CategoryDTO;
import com.example.restaurant_fcai.Entities.Category;
import com.example.restaurant_fcai.Repos.CategoryRepo;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    // Get all categories with products
    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll()
                .stream()
                .map(CategoryDTO::toDTO)
                .toList();
    }

    // Get category by ID
    public CategoryDTO getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .map(CategoryDTO::toDTO)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
    }

    // Create new category
    public CategoryDTO createCategory(Category category) {
        Category saved = categoryRepo.save(category);
        return CategoryDTO.toDTO(saved);
    }

    // Update existing category
   public CategoryDTO updateCategory(Long id, Category updatedCategory) {
    Category existing = categoryRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

    existing.setName(updatedCategory.getName());

    return CategoryDTO.toDTO(categoryRepo.save(existing));
}


    // Delete category
    public void deleteCategory(Long id) {
        if (!categoryRepo.existsById(id)) {
            throw new RuntimeException("Category not found with id " + id);
        }
        categoryRepo.deleteById(id);
    }
}
