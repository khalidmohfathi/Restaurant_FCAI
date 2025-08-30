package com.example.restaurant_fcai.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter @Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String message;
    private boolean readFlag = false;
    private Instant createdAt = Instant.now();
}
