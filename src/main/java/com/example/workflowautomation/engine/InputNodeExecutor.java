package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;
import org.springframework.stereotype.Component;

import java.util.Map;




@Component("INPUT")
public class InputNodeExecutor implements NodeExecutor {

    @Override
    public String execute(String input, WorkflowNode node, Map<String, Object> context) {
        return input; // just pass through
    }
}
