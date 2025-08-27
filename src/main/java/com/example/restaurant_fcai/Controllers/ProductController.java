package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.DTO.ProductDTO;
import com.example.restaurant_fcai.Entities.Category;
import com.example.restaurant_fcai.Entities.Menu;
import com.example.restaurant_fcai.Entities.Product;
import com.example.restaurant_fcai.Repos.ProductRepo;
import com.example.restaurant_fcai.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "delete maro");
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable("id") Long productId,
            @RequestBody Map<String, Object> updates) {

        ProductDTO updatedProduct = productService.updateProduct(productId, updates);
        return ResponseEntity.ok(updatedProduct);
    }
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Map<String, Object> payload) {
        ProductDTO createdProduct = productService.createProduct(payload);
        return ResponseEntity.ok(createdProduct);
    }

}
