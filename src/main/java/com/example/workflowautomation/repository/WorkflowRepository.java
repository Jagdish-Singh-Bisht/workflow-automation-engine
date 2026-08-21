package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.workflowautomation.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

    List<Workflow> findByUser(User user);

    boolean existsByUser(User user);

    @Query("""
        SELECT w
        FROM Workflow w
        JOIN FETCH w.user
        WHERE w.id = :workflowId
        """)

    Optional<Workflow> findByIdWithUser(@Param("workflowId") Long workflowId);

}
