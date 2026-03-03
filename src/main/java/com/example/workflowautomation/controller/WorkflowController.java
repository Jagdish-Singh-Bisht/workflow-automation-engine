package com.example.workflowautomation.controller;

import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.entity.Workflow;
import com.example.workflowautomation.engine.WorkflowEngine;
import com.example.workflowautomation.service.WorkflowService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


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


    // Run Workflow
    @PostMapping("/{workflowId}/run")
    public String runWorkflow(
            @PathVariable Long workflowId,
            @RequestParam String input) {

        return workflowEngine.runWorkflow(workflowId, input);
    }



}
