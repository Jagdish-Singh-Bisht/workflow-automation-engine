package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface WorkflowNodeRepository extends JpaRepository<WorkflowNode, Long> {

    // Get all nodes for a workflow ordered by execution sequence
    List<WorkflowNode> findByWorkflowOrderBySequenceOrderAsc(Workflow workflow);

}
