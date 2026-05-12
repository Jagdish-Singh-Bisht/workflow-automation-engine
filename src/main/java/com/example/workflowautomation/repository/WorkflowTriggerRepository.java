package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.WorkflowTrigger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WorkflowTriggerRepository extends JpaRepository<WorkflowTrigger, Long> {

    List<WorkflowTrigger> findByTriggerTypeAndIsActive(String triggerType, boolean isActive);

    Optional<WorkflowTrigger> findByWorkflowId(Long workflowId);

    long countByIsActive(boolean isActive);

}
