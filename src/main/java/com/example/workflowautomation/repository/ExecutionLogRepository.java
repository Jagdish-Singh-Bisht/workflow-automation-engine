package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.entity.ExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, Long> {

    List<ExecutionLog> findByWorkflowIdOrderByExecutedAtDesc(Long workflowId);

    List<ExecutionLog> findTop20ByOrderByExecutedAtDesc();
    List<ExecutionLog> findTop20ByWorkflowUserOrderByExecutedAtDesc(User user);

    List<ExecutionLog> findTop5ByWorkflowUserOrderByExecutedAtDesc(User user);

    long countByWorkflowUser(User user);

    long countByWorkflowUserAndStatus(User user, String status);

    void deleteByWorkflowId(Long workflowId);

}
