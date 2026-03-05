package com.example.workflowautomation.dto;


import java.time.LocalDateTime;


public class NodeExecutionLogResponse {

    private String nodeType;
    private String status;
    private LocalDateTime executedAt;

    public NodeExecutionLogResponse(String nodeType, String status, LocalDateTime executedAt) {
        this.nodeType = nodeType;
        this.status = status;
        this.executedAt = executedAt;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

}
