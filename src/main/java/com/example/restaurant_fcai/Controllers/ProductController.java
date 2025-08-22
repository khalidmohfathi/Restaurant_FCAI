package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.Entities.Category;
import com.example.restaurant_fcai.Entities.Menu;
import com.example.restaurant_fcai.Entities.Product;
import com.example.restaurant_fcai.Repos.ProductRepo;
import com.example.restaurant_fcai.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;



}
