package com.example.restaurant_fcai.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Enquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String message;
    @Column
    private boolean reply;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    public Enquiry(String message, boolean reply, Admin admin) {
        this.message = message;
        this.reply = reply;
        this.admin = admin;

    }

    public Enquiry(String message, boolean reply,  Customer customer) {
        this.message = message;
        this.reply = reply;
        this.customer = customer;

    }

}
