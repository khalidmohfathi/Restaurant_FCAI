package com.example.restaurant_fcai.Services;

import com.example.restaurant_fcai.Entities.Product;
import com.example.restaurant_fcai.Repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> product = productRepo.findById(id);
        return product.orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
}
