package com.example.workflowautomation.engine;

import com.example.workflowautomation.processor.ShipmentProcessor;
import com.example.workflowautomation.entity.Shipment;
import com.example.workflowautomation.ai.AIService;
import com.example.workflowautomation.entity.WorkflowNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.List;





@Component("AI")
@RequiredArgsConstructor
public class AINodeExecutor implements NodeExecutor {

    private final AIService aiService;

    @Override
    public String execute(String input, WorkflowNode node, Map<String, Object> context) {

        // 1. Get data (generic)
        Object dataObj = context.get("data");
        String data = (dataObj != null) ? dataObj.toString() : input;

        String type = (String) context.get("dataType");

        // 2. Shipment (keep as-is)
        if ("shipment".equalsIgnoreCase(type) && dataObj instanceof List<?>) {
            List<Shipment> shipments = (List<Shipment>) dataObj;
            return ShipmentProcessor.generateSummary(shipments);
        }

        // 3. Read prompt from configJson
        String promptInstruction = "";

        try {
            if (node.getConfigJson() != null) {
                ObjectMapper mapper = new ObjectMapper();

                Map<String, String> config =
                        mapper.readValue(node.getConfigJson(), Map.class);

                if (config.containsKey("prompt")) {
                    promptInstruction = config.get("prompt");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid config JSON, using default prompt");
        }

        // 4. Default prompt (fallback)
        if (promptInstruction == null || promptInstruction.isBlank()) {
            promptInstruction = "Summarize the following content clearly and professionally in under 80 words.";
        }

        // 5. Final prompt (controlled AI)
        String finalPrompt = """
            You are an automation AI system.

            STRICT RULES:
            - Return only final output
            - No explanations
            - No markdown
            - Keep output clean and professional
            - Follow the instruction strictly

            INSTRUCTION:
            %s

            INPUT:
            %s
            """.formatted(promptInstruction, data);

        // 6. Call AI
        try {
            return aiService.generateResponse(finalPrompt);
        } catch (Exception e) {
            e.printStackTrace();
            return data; // fallback
        }
    }
}