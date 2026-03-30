package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;



@Component("OUTPUT")
@RequiredArgsConstructor
public class OutputNodeExecutor implements NodeExecutor{

    private final ObjectMapper objectMapper;
    private final EmailService emailService;


    @Override
    public String execute(String input, WorkflowNode node, Map<String, Object> context) {

        try {
            if(node.getConfigJson() != null) {

                Map<String, String> config =
                        objectMapper.readValue(node.getConfigJson(), Map.class);

                String type = config.get("type");

                // Email Output
                if("EMAIL".equalsIgnoreCase(type)) {

                    String to = config.get("to");
                    String subject = "Automated Report";

                    emailService.sendEmail(to, subject, input);

                    System.out.println("Email sent to: " + to);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // fallback
        System.out.println("📤 FINAL OUTPUT: " + input);


        return input;
    }


}
