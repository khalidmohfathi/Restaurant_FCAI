package com.example.restaurant_fcai.Services;

import com.example.restaurant_fcai.Entities.*;
import com.example.restaurant_fcai.Repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CustomerRepo customerRepo;

    public Cart getOrCreateCart(Customer customer) {
        Optional<Cart> existingCart = cartRepo.findByCustomer(customer);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        
        Cart newCart = new Cart();
        newCart.setCustomer(customer);
        newCart.setTotalPrice(0.0);
        return cartRepo.save(newCart);
    }

    public List<CartItem> getCartItems(Long customerId) {
        Optional<Cart> cart = cartRepo.findByCustomerId(customerId);
        if (cart.isPresent()) {
            return cartItemRepo.findByCart(cart.get());
        }
        return List.of();
    }

    public CartItem addProductToCart(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        Cart cart = getOrCreateCart(customer);
        
        Optional<CartItem> existingCartItem = cartItemRepo.findByCartAndProduct(cart, product);
        
        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setPrice(product.getPrice() * cartItem.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice() * quantity);
        }
        
        cartItem = cartItemRepo.save(cartItem);
        updateCartTotalPrice(cart);
        
        return cartItem;
    }

    public boolean removeProductFromCart(Long customerId, Long productId) {
        Optional<Cart> cart = cartRepo.findByCustomerId(customerId);
        if (cart.isPresent()) {
            cartItemRepo.deleteByCartIdAndProductId(cart.get().getId(), productId);
            updateCartTotalPrice(cart.get());
            return true;
        }
        return false;
    }

    public void clearCart(Long customerId) {
        Optional<Cart> cart = cartRepo.findByCustomerId(customerId);
        if (cart.isPresent()) {
            List<CartItem> cartItems = cartItemRepo.findByCart(cart.get());
            cartItemRepo.deleteAll(cartItems);
            cart.get().setTotalPrice(0.0);
            cartRepo.save(cart.get());
        }
    }

    private void updateCartTotalPrice(Cart cart) {
        List<CartItem> cartItems = cartItemRepo.findByCart(cart);
        double totalPrice = cartItems.stream()
                .mapToDouble(CartItem::getPrice)
                .sum();
        cart.setTotalPrice(totalPrice);
        cartRepo.save(cart);
    }

    public Cart getCartByCustomerId(Long customerId) {
        return cartRepo.findByCustomerId(customerId).orElse(null);
    }
}