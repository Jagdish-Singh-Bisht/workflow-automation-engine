package com.example.workflowautomation.engine;


import org.springframework.stereotype.Component;

import java.util.Map;



@Component
public class ExecutorFactory {

    private final Map<String, NodeExecutor> executors;

    public ExecutorFactory(Map<String, NodeExecutor> executors) {
        this.executors = executors;
    }

    public NodeExecutor getExecutor(String nodeType) {
        NodeExecutor executor = executors.get(nodeType);

        if(executor == null) {
            throw new RuntimeException("No executor found for node type: " + nodeType);
        }

        return executor;
    }
}
