package com.example.workflowautomation.ai;


import org.springframework.stereotype.Service;
import com.example.workflowautomation.entity.User;


//@Service
public class MockAIService implements AIService {

    @Override
    public String generateResponse(String input, User user) {

        // Generic dynamic template (works for any input)
        return """
                Subject: Regarding %s
                
                Dear Sir/Madam,
                
                I am writing this email regarding "%s".
                
                Please consider this request.
                
                Regards,
                Workflow Automation System
               
                """.formatted(input, input);

    }

    @Override
    public String detectIntent(String input) {
        return "UNKNOWN";
    }

}
