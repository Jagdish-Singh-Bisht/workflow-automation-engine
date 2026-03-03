package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.workflowautomation.entity.User;

import java.util.List;



public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

    // Find all workflows created by a specific user;
    List<Workflow> findByUser(User user);
}
