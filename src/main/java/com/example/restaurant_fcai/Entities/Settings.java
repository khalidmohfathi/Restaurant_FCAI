package com.example.restaurant_fcai.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String logoUrl;
    private String workingHours;
    private String aboutImage;
    @Column(columnDefinition = "TEXT")
    private String aboutDescription;
    @Column(columnDefinition = "TEXT")
    private String termsAndConditions;
    private String facebookUrl;
    private String whatsappNumber;
    private String phoneNumber;
    private String secondPhoneNumber;
}
