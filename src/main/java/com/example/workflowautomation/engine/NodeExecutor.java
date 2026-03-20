package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;

import java.util.Map;


public interface NodeExecutor {
    String execute(String input, WorkflowNode node, Map<String, Object> context);
}
