package com.example.workflowautomation.engine;


import org.springframework.stereotype.Component;


@Component("OUTPUT")
public class OutputNodeExecutor implements NodeExecutor{

    @Override
    public String execute(String input) {
        return "[FINAL OUTPUT]: " + input;
    }
}
