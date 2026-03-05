package com.example.workflowautomation.ai;


import org.springframework.stereotype.Service;



//@Service
public class MockAIService implements AIService {

    @Override
    public String generateResponse(String input) {

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
}
