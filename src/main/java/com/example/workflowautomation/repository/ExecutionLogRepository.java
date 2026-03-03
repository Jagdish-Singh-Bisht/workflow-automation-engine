package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.ExecutionLog;
import com.example.workflowautomation.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, Long> {

    // Get execution logs history for a workflow
    List<ExecutionLog> findByWorkflow(Workflow workflow);

}
