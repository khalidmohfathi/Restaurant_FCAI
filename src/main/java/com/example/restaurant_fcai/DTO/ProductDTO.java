package com.example.restaurant_fcai.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean available;
    private String image;

    private Long categoryId;
    private String categoryName;

    private Long cartItemId;
    private int cartItemQuantity;
    private double cartItemPrice;
}