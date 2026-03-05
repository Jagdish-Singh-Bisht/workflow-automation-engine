package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;
import org.springframework.stereotype.Component;


@Component("OUTPUT")
public class OutputNodeExecutor implements NodeExecutor{

    @Override
    public String execute(String input, WorkflowNode node) {
        return "[FINAL OUTPUT]: " + input;
    }
}
