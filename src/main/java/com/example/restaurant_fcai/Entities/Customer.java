package com.example.restaurant_fcai.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Customer extends User {

    public Customer(String name, String email, String password, String phone) {
        super(name, email, password, phone);
    }

    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Enquiry> enquiry;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Order> order;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Notification> notification;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Rate> rate;
}
