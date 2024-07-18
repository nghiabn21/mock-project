package com.example.mockproject.repository;


import com.example.mockproject.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    @Query(value = "from Skill s where s.name = ?1")
    Skill findByName(String name);

    Skill findByNameIgnoreCase(String name);

    boolean existsByName(String skillName);

    @Query(value = "select s.name from Skill s")
    List<String> findAllName();
}
