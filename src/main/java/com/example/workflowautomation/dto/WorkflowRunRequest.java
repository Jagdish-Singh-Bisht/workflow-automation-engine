package com.example.workflowautomation.dto;


import lombok.Data;


@Data
public class WorkflowRunRequest {

    private Long workflowId;
    private String input;

    private String taskName;
    private String description;
    private String date;

    private Long taskId;

    private String audience;

    private Boolean emailEnabled;
    private Boolean whatsappEnabled;


}
