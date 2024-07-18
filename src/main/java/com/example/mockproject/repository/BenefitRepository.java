package com.example.mockproject.repository;

import com.example.mockproject.model.Benefit;
import com.example.mockproject.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Integer> {
    @Query(value = "from Benefit b where b.name=?1")
    Benefit findByName(String name);

    Benefit findByNameIgnoreCase(String name);


}