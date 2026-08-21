package com.example.workflowautomation.engine;


import com.example.workflowautomation.exception.WorkflowConfigurationException;
import com.example.workflowautomation.entity.EmailRecipient;
import com.example.workflowautomation.service.EmailRecipientService;
import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.service.EmailService;
import com.example.workflowautomation.service.WhatsAppService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.List;


@Component("OUTPUT")
@RequiredArgsConstructor
public class OutputNodeExecutor implements NodeExecutor{

    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;
    private final EmailRecipientService emailRecipientService;



    @Override
    public String execute(String input, WorkflowNode node, Map<String, Object> context) {

        User workflowOwner = (User) context.get("user");

        Boolean emailEnabled = (Boolean)
                context.get("emailEnabled");

        Boolean whatsappEnabled = (Boolean)
                context.get("whatsappEnabled");


        if(Boolean.TRUE.equals(whatsappEnabled)) {
            System.out.println(input);
            whatsAppService.sendWhatsapp(workflowOwner,
                    "whatsapp:" + workflowOwner.getWhatsappNumber(),
                    input);
            return "Sent via WhatsApp";
        }

        if(Boolean.TRUE.equals(emailEnabled)) {

            List<EmailRecipient> recipients =
                    emailRecipientService.getActiveRecipients(workflowOwner);

            if(recipients.isEmpty()) {
                throw new WorkflowConfigurationException(
                        "No active email recipients configured. " +
                                "Please add and activate at least one email recipient from Profile → Email Recipients."
                );

            }

            String subject = "Automated Report";

            for(EmailRecipient recipient : recipients) {

                emailService.sendEmail(
                        workflowOwner,
                        recipient.getEmail(),
                        subject,
                        input
                );

                System.out.println("Email sent to: " + recipient.getEmail());

            }

             return "Sent via Email";
        }



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

                    if(whatsappEnabled == null || Boolean.TRUE.equals(whatsappEnabled)) {

                        whatsAppService.sendWhatsapp(
                                workflowOwner,
                                "whatsapp:" + workflowOwner.getWhatsappNumber(),
                                output
                        );

                        return "Sent via WhatsApp";

                    } else if (Boolean.TRUE.equals(emailEnabled)) {

                        List<EmailRecipient> recipients =
                                emailRecipientService.getActiveRecipients(workflowOwner);

                        if(recipients.isEmpty()) {
                            throw new WorkflowConfigurationException(
                                    "No active email recipients configured. " +
                                            "Please add and activate at least one email recipient from Profile → Email Recipients."
                            );

                        }

                        String subject = (String) config.getOrDefault(
                                "subject",
                                "Automated Report"
                        );

                        for(EmailRecipient recipient : recipients) {

                            emailService.sendEmail(
                                    workflowOwner,
                                    recipient.getEmail(),
                                    subject,
                                    output
                            );

                            System.out.println("Fallback -> Email sent to: " + recipient.getEmail());

                        }

                        return "Fallback -> Sent via Email";
                    }

                } else if ("EMAIL".equalsIgnoreCase(finalType)) {

                    if(emailEnabled == null || Boolean.TRUE.equals(emailEnabled)) {

                        List<EmailRecipient> recipients =
                                emailRecipientService.getActiveRecipients(workflowOwner);

                        if (recipients.isEmpty()) {
                            throw new WorkflowConfigurationException(
                                    "No active email recipients configured. " +
                                            "Please add and activate at least one email recipient from Profile → Email Recipients."
                            );
                        }

                        String subject = (String) config.getOrDefault(
                                "subject",
                                "Automated Report"
                        );

                        for (EmailRecipient recipient : recipients) {

                            emailService.sendEmail(
                                    workflowOwner,
                                    recipient.getEmail(),
                                    subject,
                                    output
                            );

                            System.out.println("Email sent to: " + recipient.getEmail());

                        }

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
