package com.example.workflowautomation.repository;

import com.example.workflowautomation.entity.NodeExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface NodeExecutionLogRepository extends JpaRepository<NodeExecutionLog, Long> {

    List<NodeExecutionLog> findByWorkflowIdOrderByExecutedAtDesc(Long workflowId);
}
