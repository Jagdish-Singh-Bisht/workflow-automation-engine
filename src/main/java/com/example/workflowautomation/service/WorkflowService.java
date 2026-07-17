package com.example.workflowautomation.service;



import com.example.workflowautomation.exception.WorkflowAccessDeniedException;
import com.example.workflowautomation.dto.ExecutionLogResponse;
import com.example.workflowautomation.dto.NodeExecutionLogResponse;

import com.example.workflowautomation.repository.WorkflowTriggerRepository;
import com.example.workflowautomation.entity.Workflow;
import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.entity.ExecutionLog;
import com.example.workflowautomation.entity.NodeExecutionLog;

import com.example.workflowautomation.repository.WorkflowRepository;
import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.repository.WorkflowNodeRepository;
import com.example.workflowautomation.repository.ExecutionLogRepository;
import com.example.workflowautomation.repository.NodeExecutionLogRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.workflowautomation.security.CustomUserDetails;



@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowNodeRepository workflowNodeRepository;
    private final UserRepository userRepository;
    private final ExecutionLogRepository executionLogRepository;

    private final NodeExecutionLogRepository nodeExecutionLogRepository;
    private final WorkflowTriggerRepository workflowTriggerRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser();
    }

    // Create Workflow
    public Workflow createWorkflow(String workflowName) {

        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        Workflow workflow = Workflow.builder()
                .name(workflowName)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        // return workflowRepository.save(workflow);

        // replace by this
        Workflow savedWorkflow = workflowRepository.save(workflow);

        addNode(savedWorkflow.getId(), "INPUT", 1, null);

        addNode(
                savedWorkflow.getId(),
                "AI",
                2,
                "{\"prompt\":\"Convert into a professional email under 80 words\"}"
        );

        addNode(
                savedWorkflow.getId(),
                "OUTPUT",
                3,
                "{\"type\":\"EMAIL\"}"
        );

        return savedWorkflow;

    }

    public Workflow getWorkflowForCurrentUser(Long workflowId) {

        User currentUser = getCurrentUser();

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        if (!workflow.getUser().getId().equals(currentUser.getId())) {
            throw new WorkflowAccessDeniedException(
                    "You are not allowed to access this workflow."
            );
        }

        return workflow;

    }


    public List<Workflow> getCurrentUserWorkflows() {

        User currentUser = getCurrentUser();

        return workflowRepository.findByUser(currentUser);
    }

    public long getCurrentUserWorkflowCount() {
        return getCurrentUserWorkflows().size();
    }

    // Add Node to Workflow
    public WorkflowNode addNode(Long workflowId,
                                String nodeType,
                                Integer sequenceOrder,
                                String configJson) {

        Workflow workflow = getWorkflowForCurrentUser(workflowId);

        WorkflowNode node = WorkflowNode.builder()
                .workflow(workflow)
                .nodeType(nodeType)
                .sequenceOrder(sequenceOrder)
                .configJson(configJson)
                .build();

        return workflowNodeRepository.save(node);
    }



    // Get Nodes in Execution Order
    public List<WorkflowNode> getOrderedNodes(Long workflowId) {

        Workflow workflow = getWorkflowForCurrentUser(workflowId);

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

    public long getCurrentUserExecutionCount() {

        return executionLogRepository.countByWorkflowUser(getCurrentUser());

    }

    public long getCurrentUserSuccessfulExecutionCount() {

        return executionLogRepository.countByWorkflowUserAndStatus(
                getCurrentUser(),
                "SUCCESS"
        );

    }

    public long getCurrentUserFailedExecutionCount() {

        return executionLogRepository.countByWorkflowUserAndStatus(
                getCurrentUser(),
                "FAILED"
        );
    }

    public List<NodeExecutionLogResponse> getNodeExecutionHistory(Long workflowId) {

        List<NodeExecutionLog> logs =
                nodeExecutionLogRepository.findByWorkflowIdOrderByExecutedAtDesc(workflowId);

        return logs.stream()
                .map(log -> new NodeExecutionLogResponse(
                        log.getNodeType(),
                        log.getStatus(),
                        log.getExecutedAt()
                ))
                .toList();

    }

    public List<ExecutionLog> getCurrentUserRecentExecutions() {

        return executionLogRepository.findTop5ByWorkflowUserOrderByExecutedAtDesc(getCurrentUser());
    }

    public ExecutionLog getCurrentUserLastExecution() {

        List<ExecutionLog> recent = getCurrentUserRecentExecutions();

        return recent.isEmpty() ? null : recent.get(0);
    }
    public List<Workflow> getAllWorkflows() {
        return workflowRepository.findAll();
    }

    public long getCurrentUserActiveTriggerCount() {

        List<Workflow> workflows = getCurrentUserWorkflows();

        List<Long> workflowIds = workflows.stream()
                .map(Workflow::getId)
                .toList();

        if(workflowIds.isEmpty()) {
            return 0;
        }

        return workflowTriggerRepository.countByWorkflowIdInAndIsActive(workflowIds, true);
    }

}
