package com.example.workflowautomation.engine;


import com.example.workflowautomation.ai.AIService;
import com.example.workflowautomation.entity.WorkflowNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.Map;


@Component("AI")
@RequiredArgsConstructor
public class AINodeExecutor implements NodeExecutor {

    private final AIService aiService;

    @Override
    public String execute(String input, WorkflowNode node, Map<String, Object> context) {

        String taskName = (String) context.get("taskName");
        String description = (String) context.get("description");
        String date = (String) context.get("date");

        // fallback
        if(taskName == null) taskName = "General Task";
        if(description == null) description = "";
        if(date == null) date = "";



        String prompt = "";

        try {
            if(node.getConfigJson() != null) {
                ObjectMapper mapper = new ObjectMapper();

                Map<String, String> config =
                        mapper.readValue(node.getConfigJson(), Map.class);

                if(config.containsKey("prompt")) {
                    prompt = config.get("prompt");
                }
            }
        } catch(Exception e) {
            System.out.println("Invalid JSON config, using default prompt");

            prompt = "Generate a professional email with REAL values. Do NOT use placeholders like [Date], [Name], [Time] and any markdown like (**, ##, etc.). Fill everything with realistic values. Return only final output.    ";

        }





        String finalPrompt = """
                You are an automation system.
                
                STRICT RULES:
                - Return only final output
                - No explanations
                - No markdown (**, ##, etc)
                - No placeholders like [Date], [Name], etc.
                - Fill all details with realistic values
                - No multiple options
                - Do not invent unrelated scenarios and not change the context 
                - Keep response under 120 words
                - Output must be in clean email format
                
                Generate a professional email based ONLY on the following details: 
                
                
                EMAIL FORMAT:
                
                Subject: <short subject>
                
                Dear Team,
                
                <clear professional message using given data>
                
                Regards,
                Automation System
                
                
                
                TASK DETAILS:
                Task Name: %s
                Description: %s
                Date: %s
                
                Instruction:
                %s
                
                """.formatted(taskName, description, date, input);


        return aiService.generateResponse(finalPrompt);
    }

}
