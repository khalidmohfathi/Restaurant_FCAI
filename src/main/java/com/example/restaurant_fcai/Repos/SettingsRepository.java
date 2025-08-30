package com.example.restaurant_fcai.Repos;

import com.example.restaurant_fcai.Entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restaurant_fcai.Entities.Category;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

}
