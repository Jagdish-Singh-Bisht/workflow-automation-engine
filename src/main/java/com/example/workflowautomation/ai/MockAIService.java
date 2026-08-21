package com.example.workflowautomation.ai;



import com.example.workflowautomation.entity.User;


public class MockAIService implements AIService {

    @Override
    public String generateResponse(String input, User user) {

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
