package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface WorkflowNodeRepository extends JpaRepository<WorkflowNode, Long> {

    List<WorkflowNode> findByWorkflowOrderBySequenceOrderAsc(Workflow workflow);

    void deleteByWorkflow(Workflow workflow);

}
