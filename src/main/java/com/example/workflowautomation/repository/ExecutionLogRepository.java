package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.entity.ExecutionLog;
import com.example.workflowautomation.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, Long> {

    // Get execution logs history for a workflow
//    List<ExecutionLog> findByWorkflow(Workflow workflow);

    List<ExecutionLog> findByWorkflowIdOrderByExecutedAtDesc(Long workflowId);

    // ExecutionLog findTopByOrderByExecutedAtDesc();

    List<ExecutionLog> findTop20ByOrderByExecutedAtDesc();
    List<ExecutionLog> findTop20ByWorkflowUserOrderByExecutedAtDesc(User user);


//    List<ExecutionLog> findTop5ByOrderByExecutedAtDesc();
    List<ExecutionLog> findTop5ByWorkflowUserOrderByExecutedAtDesc(User user);

//    long countByStatus(String status);

    long countByWorkflowUser(User user);

    long countByWorkflowUserAndStatus(User user, String status);

}
