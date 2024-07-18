package com.example.mockproject.repository;

import com.example.mockproject.model.Benefit;
import com.example.mockproject.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level, Integer> {
    Level findByName(String level);
    Level findByNameIgnoreCase(String name);

}
