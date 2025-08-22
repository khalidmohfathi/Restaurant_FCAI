package com.example.restaurant_fcai.Entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@AllArgsConstructor
//@NoArgsConstructor
@Setter
@Getter
public class Cashier extends User {

    public Cashier(String name, String email, String password, String phone) {
        super(name, email, password, phone);
    }

}
