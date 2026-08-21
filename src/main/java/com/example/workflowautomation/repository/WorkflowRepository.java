package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.workflowautomation.entity.User;

import java.util.List;



public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

    List<Workflow> findByUser(User user);

    boolean existsByUser(User user);
}
