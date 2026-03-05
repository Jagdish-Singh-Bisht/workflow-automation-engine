package com.example.workflowautomation.service;


import com.example.workflowautomation.dto.ExecutionLogResponse;
import com.example.workflowautomation.entity.ExecutionLog;
import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.entity.Workflow;
import com.example.workflowautomation.repository.ExecutionLogRepository;
import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.repository.WorkflowNodeRepository;
import com.example.workflowautomation.repository.WorkflowRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;




@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowNodeRepository workflowNodeRepository;
    private final UserRepository userRepository;
    private final ExecutionLogRepository executionLogRepository;



    // Create Workflow
    public Workflow createWorkflow(String username, String workflowName) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Workflow workflow = Workflow.builder()
                .name(workflowName)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return workflowRepository.save(workflow);

    }



    // Add Node to Workflow
    public WorkflowNode addNode(Long workflowId, String nodeType, Integer sequenceOrder) {

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        WorkflowNode node = WorkflowNode.builder()
                .workflow(workflow)
                .nodeType(nodeType)
                .sequenceOrder(sequenceOrder)
                .build();

        return workflowNodeRepository.save(node);
    }



    // Get Nodes in Execution Order
    public List<WorkflowNode> getOrderedNodes(Long workflowId) {

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        return workflowNodeRepository
                .findByWorkflowOrderBySequenceOrderAsc(workflow);
    }




    public List<ExecutionLogResponse> getExecutionHistory(Long workflowId) {

        List<ExecutionLog> logs =
                executionLogRepository.findByWorkflowIdOrderByExecutedAtDesc(workflowId);

        return logs.stream()
                .map(log -> new ExecutionLogResponse(
                        log.getId(),
                        log.getInputData(),
                        log.getOutputData(),
                        log.getStatus(),
                        log.getExecutedAt()
                ))
                .toList();

    }


}
