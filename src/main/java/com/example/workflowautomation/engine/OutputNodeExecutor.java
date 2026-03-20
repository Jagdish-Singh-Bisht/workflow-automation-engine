package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;
import org.springframework.stereotype.Component;

import java.util.Map;



@Component("OUTPUT")
public class OutputNodeExecutor implements NodeExecutor{

    @Override
    public String execute(String input, WorkflowNode node, Map<String, Object> context) {
        return "[FINAL OUTPUT]: " + input;
    }
}
