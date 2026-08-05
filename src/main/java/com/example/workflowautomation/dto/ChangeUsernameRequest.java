package com.example.workflowautomation.dto;


import lombok.Data;

@Data
public class ChangeUsernameRequest {

    private String currentPassword;

    private String newUsername;
}
