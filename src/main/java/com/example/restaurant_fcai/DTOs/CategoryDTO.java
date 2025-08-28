package com.example.restaurant_fcai.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private Long menuId;
    private List<ProductDTO> products;

    public static CategoryDTO toDTO(com.example.restaurant_fcai.Entities.Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getMenu() != null ? category.getMenu().getId() : null,
                category.getProduct() != null
                        ? category.getProduct().stream()
                              .map(product -> new ProductDTO(
                                      product.getId(),
                                      product.getName(),
                                      product.getDescription(),
                                      product.getPrice(),
                                      product.isAvailable(),
                                      product.getImage(),
                                      product.getCategory() != null ? product.getCategory().getId() : null,
                                      product.getCategory() != null ? product.getCategory().getName() : null,
                                      product.getCartItem() != null ? product.getCartItem().getId() : null,
                                      product.getCartItem() != null ? product.getCartItem().getQuantity() : 0,
                                      product.getCartItem() != null ? product.getCartItem().getPrice() : 0.0
                              ))
                              .toList()
                        : List.of()
        );
    }
}
