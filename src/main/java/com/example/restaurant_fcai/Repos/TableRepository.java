package com.example.restaurant_fcai.Repos;

import com.example.restaurant_fcai.Entities.CustomerTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<CustomerTable, Long> {

}