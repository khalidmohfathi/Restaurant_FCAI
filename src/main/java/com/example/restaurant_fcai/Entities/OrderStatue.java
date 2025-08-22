package com.example.restaurant_fcai.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderStatue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Date createdAt;

    @Column
    private String status;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderTable orderTable;

    public OrderStatue(OrderTable orderTable,  String status,  Date createdAt) {
        this.orderTable = orderTable;
        this.status = status;
        this.createdAt = createdAt;
    }
}
