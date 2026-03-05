package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;
import org.springframework.stereotype.Component;


@Component("INPUT")
public class InputNodeExecutor implements NodeExecutor {

    @Override
    public String execute(String input, WorkflowNode node) {
        return input; // just pass through
    }
}
