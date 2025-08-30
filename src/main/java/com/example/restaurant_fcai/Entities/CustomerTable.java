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
public class CustomerTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String qrCode;
    @Column
    private int number;

    @OneToMany(mappedBy = "customerTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    public CustomerTable(String qrCode, int number) {}
}
