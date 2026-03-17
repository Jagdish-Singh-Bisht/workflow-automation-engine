package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.WorkflowTrigger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface WorkflowTriggerRepository extends JpaRepository<WorkflowTrigger, Long> {

    List<WorkflowTrigger> findByTriggerTypeAndIsActive(String triggerType, boolean isActive);

}
