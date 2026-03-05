package com.example.workflowautomation.engine;


import com.example.workflowautomation.ai.AIService;

import com.example.workflowautomation.entity.WorkflowNode;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;




@Component("AI")
@RequiredArgsConstructor
public class AINodeExecutor implements NodeExecutor {

    private final AIService aiService;

    @Override
    public String execute(String input, WorkflowNode node) {

        String prompt = "";

        if(node.getConfigJson() != null) {
            prompt = node.getConfigJson();
        }

        String finalPrompt = prompt + " " + input;

        return aiService.generateResponse(input);
    }

}
