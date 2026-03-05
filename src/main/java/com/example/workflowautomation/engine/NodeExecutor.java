package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;


public interface NodeExecutor {
    String execute(String input, WorkflowNode node);
}
