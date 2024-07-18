package com.example.mockproject.repository;

import com.example.mockproject.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phone);

}
