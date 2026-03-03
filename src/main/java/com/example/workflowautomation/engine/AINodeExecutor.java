package com.example.workflowautomation.engine;


import org.springframework.stereotype.Component;


@Component("AI")
public class AINodeExecutor implements NodeExecutor {

    @Override
    public String execute(String input) {
        return "{AI PROCESSES}: " + input;
    }
}
