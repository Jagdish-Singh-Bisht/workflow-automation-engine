package com.example.workflowautomation.service;


import com.example.workflowautomation.dto.ChangePasswordRequest;
import com.example.workflowautomation.dto.ChangeUsernameRequest;



public interface ProfileService {

    void changePassword(ChangePasswordRequest request);

    void changeUsername(ChangeUsernameRequest request);
}
