package com.example.workflowautomation.service;


import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.dto.RegisterRequest;


public interface UserService {

    User registerUser(RegisterRequest request);
}
