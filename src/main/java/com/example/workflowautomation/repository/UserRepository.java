package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findAllByOrderByCreatedAtDesc();

    long countByRole(String role);
}
