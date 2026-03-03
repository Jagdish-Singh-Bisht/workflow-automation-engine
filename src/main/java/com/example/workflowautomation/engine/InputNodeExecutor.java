package com.example.workflowautomation.engine;


import org.springframework.stereotype.Component;


@Component("INPUT")
public class InputNodeExecutor implements NodeExecutor {

    @Override
    public String execute(String input) {
        return input; // just pass through
    }
}
