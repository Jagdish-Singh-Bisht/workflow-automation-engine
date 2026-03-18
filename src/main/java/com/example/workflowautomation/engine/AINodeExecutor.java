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
    public String execute(String input, WorkflowNode node) {

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

            prompt = "Generate a professional email with REAL values. Do NOT use placeholders like [Date], [Name], [Time]. Fill everything with realistic values. Return only final output.";
        }


        String finalPrompt = """
                You are an automation system.
                
                STRICT RULES:
                - Return only final output
                - No explanations
                - No placeholders like [Date], [Name], etc.
                - Fill all details with realistic values
                - No multiple options
                - Keep response under 120 words
                
                TASK:
                %s %s
                
                """.formatted(prompt, input);


        return aiService.generateResponse(finalPrompt);
    }

}
