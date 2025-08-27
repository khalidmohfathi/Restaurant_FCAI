package com.example.restaurant_fcai.Services;
import com.example.restaurant_fcai.DTO.ProductDTO;
import com.example.restaurant_fcai.Entities.CartItem;
import com.example.restaurant_fcai.Entities.Product;
import com.example.restaurant_fcai.Repos.CartItemRepo;
import com.example.restaurant_fcai.Repos.CategoryRepo;
import com.example.restaurant_fcai.Repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private CategoryRepo categoryRepo;
// create product
    public ProductDTO createProduct(Map<String, Object> payload) {
        Product product = new Product();

        product.setName((String) payload.get("name"));
        product.setDescription((String) payload.get("description"));
        product.setPrice(Double.parseDouble(payload.get("price").toString()));
        product.setAvailable((Boolean) payload.get("available"));
        product.setImage((String) payload.get("image"));


        if (payload.containsKey("categoryId")) {
            Long categoryId = Long.parseLong(payload.get("categoryId").toString());
            product.setCategory(categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId)));
        }


        if (payload.containsKey("cartItemId")) {
            Long cartItemId = Long.parseLong(payload.get("cartItemId").toString());
            product.setCartItem(cartItemRepo.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("CartItem not found with id " + cartItemId)));
        }

        Product savedProduct = productRepo.save(product);
        return convertToDTO(savedProduct);
    }

    // update Product
    public ProductDTO updateProduct(Long productId, Map<String, Object> updates) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        if (updates.containsKey("name")) {
            product.setName((String) updates.get("name"));
        }
        if (updates.containsKey("description")) {
            product.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("price")) {
            product.setPrice(Double.parseDouble(updates.get("price").toString()));
        }
        if (updates.containsKey("available")) {
            product.setAvailable((Boolean) updates.get("available"));
        }
        if (updates.containsKey("image")) {
            product.setImage((String) updates.get("image"));
        }

        Product savedProduct = productRepo.save(product);
        return convertToDTO(savedProduct);
    }
// delete Product
    public void deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        CartItem cartItem = product.getCartItem();


        productRepo.delete(product);

        if (cartItem.getProduct() == null || cartItem.getProduct().isEmpty()) {
            cartItemRepo.delete(cartItem);
        }
    }


// get All Products
    public List<ProductDTO> getAllProducts() {
        return productRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
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
        );
    }
// get specific Product
    public ProductDTO getProductById(Long id) {
        return productRepo.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }
}
