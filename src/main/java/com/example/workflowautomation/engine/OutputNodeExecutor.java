package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.service.EmailService;
import com.example.workflowautomation.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;



@Component("OUTPUT")
@RequiredArgsConstructor
public class OutputNodeExecutor implements NodeExecutor{

    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;



    @Override
    public String execute(String input, WorkflowNode node, Map<String, Object> context) {

        Boolean emailEnabled = (Boolean)
                context.get("emailEnabled");

        Boolean whatsappEnabled = (Boolean)
                context.get("whatsappEnabled");


        try {
            if(node.getConfigJson() != null) {

                Map<String, Object> config =
                        objectMapper.readValue(node.getConfigJson(), Map.class);

                String type = (String) config.get("type");

                String output = input;
                String lower = output.toLowerCase();

                // S1: Identify route
                String routeKey = "default";

                if(lower.contains("internship")) {
                    routeKey = "internship";
                } else if(lower.contains("exam")) {
                    routeKey = "exam";
                }

                // S2: Routing config
                Map<String, Object> routes =
                        (Map<String, Object>) config.get("routes");

                String finalType = type;

                if(routes != null) {
                    finalType = (String) routes.getOrDefault(routeKey, type);
                }

                System.out.println("Route: " + routeKey + " -> " + finalType);


                // S3: Execute based on type
                if("WHATSAPP".equalsIgnoreCase(finalType)) {

                    if(Boolean.TRUE.equals(whatsappEnabled)) {

                        whatsAppService.sendWhatsapp(output);
                        return "Sent via WhatsApp";

                    } else if (Boolean.TRUE.equals(emailEnabled)) {

                        // Fallback to email if WhatsApp is disabled
                        String to = (String) config.getOrDefault("to", "jbisht526@gmail.com");
                        String subject = "Automated Report";

                        emailService.sendEmail(to, subject, output);

                        System.out.println("Fallback -> Email sent to: " + to);
                        return "Fallback -> Sent via Email";

                    }


                } else if ("EMAIL".equalsIgnoreCase(finalType)) {

                    if(Boolean.TRUE.equals(emailEnabled)) {

                        String to = (String) config.getOrDefault("to", "jbisht526@gmail.com");
                        String subject = "Automated Report";

                        emailService.sendEmail(to, subject, output);
                        System.out.println("Email sent to: " + to);

                        return "Sent via Email";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // fallback
        System.out.println("FINAL OUTPUT: " + input);

        return input;
    }


}
