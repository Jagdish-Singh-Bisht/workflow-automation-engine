package com.example.workflowautomation.dto;


import java.time.LocalDateTime;


public class ExecutionLogResponse {

    private Long id;
    private String inputData;
    private String outputData;
    private String status;
    private LocalDateTime executedAt;


    public ExecutionLogResponse(
            Long id,
            String inputData,
            String outputData,
            String status,
            LocalDateTime executedAt) {


        this.id = id;
        this.inputData = inputData;
        this.outputData = outputData;
        this.status = status;
        this.executedAt = executedAt;
    }


    public Long getId() {
        return id;
    }

    public String getInputData() {
        return inputData;
    }

    public String getOutputData() {
        return outputData;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

}
