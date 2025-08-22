package com.example.restaurant_fcai.Services;

import com.example.restaurant_fcai.Entities.Product;
import com.example.restaurant_fcai.Repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Product saveProduct (Product product) {
      return  productRepo.save(product);
    }
}
