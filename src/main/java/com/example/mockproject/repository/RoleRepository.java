package com.example.mockproject.repository;

import com.example.mockproject.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    @Query(value = "SELECT r.role_id, r.name from roles r where r.name = ?1",nativeQuery = true)
    Optional<Roles> findByName(String roleName);
}
