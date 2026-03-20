package com.example.workflowautomation.controller;

import com.example.workflowautomation.dto.ExecutionLogResponse;
import com.example.workflowautomation.dto.NodeExecutionLogResponse;
import com.example.workflowautomation.dto.WorkflowRunRequest;
import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.entity.Workflow;
import com.example.workflowautomation.engine.WorkflowEngine;
import com.example.workflowautomation.service.WorkflowService;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;


@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;
    private final WorkflowEngine workflowEngine;


    // Create Workflow
    @PostMapping("/create")
    public Workflow createWorkflow(
            @RequestParam String username,
            @RequestParam String workflowName) {

        return workflowService.createWorkflow(username, workflowName);
    }


    // Add Node
    @PostMapping("/{workflowId}/nodes")
    public WorkflowNode addNode(
            @PathVariable Long workflowId,
            @RequestParam String nodeType,
            @RequestParam int sequenceOrder) {

        return workflowService.addNode(workflowId, nodeType, sequenceOrder);
    }


    /*
    // Run Workflow
    @PostMapping("/{workflowId}/run")
    public String runWorkflow(
            @PathVariable Long workflowId,
            @RequestParam String input) {

        return workflowEngine.runWorkflow(workflowId, input);
    }


     */


    @PostMapping("/{workflowId}/run")
    public String runWorkflow(
            @PathVariable Long workflowId,
            @RequestParam String input) {

        WorkflowRunRequest request = new WorkflowRunRequest();
        request.setWorkflowId(workflowId);
        request.setInput(input);

        return workflowEngine.runWorkflow(request);
    }


    @GetMapping("/{workflowId}/executions")
    public List<ExecutionLogResponse> getExecutionHistory(@PathVariable Long workflowId) {
        return workflowService.getExecutionHistory(workflowId);
    }


    @GetMapping("/{workflowId}/node/executions")
    public List<NodeExecutionLogResponse> getNodeExecutionHistory(@PathVariable Long workflowId) {
        return workflowService.getNodeExecutionHistory(workflowId);
    }


    @PostMapping("/run")
    public String runWorkflow(@RequestBody WorkflowRunRequest request) {

        return workflowEngine.runWorkflow(request);
    }

}


