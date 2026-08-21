package com.example.workflowautomation.ai;

import com.example.workflowautomation.entity.User;

public interface AIService {

    String generateResponse(String input, User user);

    String detectIntent(String input);

}
