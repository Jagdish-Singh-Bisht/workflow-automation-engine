package com.example.workflowautomation.engine;

import com.example.workflowautomation.entity.EmailRecipient;
import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.service.EmailRecipientService;
import com.example.workflowautomation.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component("EMAIL_SEND")
public class EmailSendNodeExecutor implements NodeExecutor {

    private final EmailService emailService;
    private final EmailRecipientService emailRecipientService;
    private final ObjectMapper objectMapper;


    public EmailSendNodeExecutor(
            EmailService emailService,
            EmailRecipientService emailRecipientService,
            ObjectMapper objectMapper) {

        this.emailService = emailService;
        this.emailRecipientService = emailRecipientService;
        this.objectMapper = objectMapper;
    }


    @Override
    public String execute(String input,
                          WorkflowNode node,
                          Map<String, Object> context) {

        User workflowOwner =
                (User) context.get("user");


        List<EmailRecipient> recipients =
                emailRecipientService
                        .getCurrentUserActiveRecipients();


        if (recipients.isEmpty()) {

            throw new RuntimeException(
                    "No active email recipients configured"
            );
        }


        String taskName =
                (String) context.get("taskName");

        if (taskName == null || taskName.isBlank()) {
            taskName = "Task";
        }

        String subject =
                taskName + " - Notification";


        try {

            if (node.getConfigJson() != null) {

                Map<String, String> config =
                        objectMapper.readValue(
                                node.getConfigJson(),
                                Map.class
                        );

                if (config.containsKey("subject")) {

                    subject =
                            config.get("subject");
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid config_json format",
                    e
            );
        }


        for (EmailRecipient recipient : recipients) {

            System.out.println(
                    "Sending email to: "
                            + recipient.getEmail()
            );

            emailService.sendEmail(
                    workflowOwner,
                    recipient.getEmail(),
                    subject,
                    input
            );
        }


        return "EMAIL SENT SUCCESSFULLY\n\n" + input;
    }
}