package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.Entities.Category;
import com.example.restaurant_fcai.Entities.Product;
import com.example.restaurant_fcai.Repos.CategoryRepo;
import com.example.restaurant_fcai.Repos.ProductRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class MenuController {

    private final CategoryRepo catRepo;
    private final ProductRepo prodRepo;

    public MenuController(CategoryRepo catRepo, ProductRepo prodRepo) {
        this.catRepo = catRepo;
        this.prodRepo = prodRepo;
    }

    @GetMapping("/categories")
    public List<Category> listCategories() {
        return catRepo.findAll();
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category c) {
        return catRepo.save(c);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category input) {
        return catRepo.findById(id).map(c -> {
            if (input.getName() != null) c.setName(input.getName());
            return ResponseEntity.ok(catRepo.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (!catRepo.existsById(id)) return ResponseEntity.notFound().build();
        prodRepo.findAll().stream().filter(p -> Objects.equals(p.getCategoryId(), id))
                .forEach(p -> prodRepo.deleteById(p.getId()));
        catRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    public List<Product> listProducts(@RequestParam(required = false) Long categoryId) {
        List<Product> all = prodRepo.findAll();
        return all.stream().filter(p -> categoryId == null || Objects.equals(p.getCategoryId(), categoryId)).toList();
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product p) {
        if (p.getCategoryId() == null || !catRepo.existsById(p.getCategoryId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(prodRepo.save(p));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product input) {
        return prodRepo.findById(id).map(p -> {
            if (input.getName() != null) p.setName(input.getName());
            if (input.getDescription() != null) p.setDescription(input.getDescription());
            if (input.getPrice() != null) p.setPrice(input.getPrice());
            if (input.getImage() != null) p.setImage(input.getImage());
            if (input.getIsAvailable() != null) p.setIsAvailable(input.getIsAvailable());
            if (input.getCategory() != null && catRepo.existsById(input.getCategoryId()))
                p.setCategoryId(input.getCategoryId());
            return ResponseEntity.ok(prodRepo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!prodRepo.existsById(id)) return ResponseEntity.notFound().build();
        prodRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
