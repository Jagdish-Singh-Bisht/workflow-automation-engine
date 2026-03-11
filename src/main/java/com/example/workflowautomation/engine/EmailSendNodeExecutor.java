package com.example.workflowautomation.engine;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.service.EmailService;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component("EMAIL_SEND")
public class EmailSendNodeExecutor implements NodeExecutor{

    private final EmailService emailService;

    public EmailSendNodeExecutor(EmailService emailService) {
        this.emailService = emailService;
    }


    @Override
    public String execute(String input, WorkflowNode node) {

        String to = "jbisht526@gmail.com";
        String subject = "Automated Notice";

        try{

            if(node.getConfigJson() != null) {
                ObjectMapper mapper = new ObjectMapper();

                Map<String, String> config =
                        mapper.readValue(node.getConfigJson(), Map.class);

                if(config.containsKey("to")) {
                    to = config.get("to");
                }

                if(config.containsKey("subject")) {
                    subject = config.get("subject");
                }
            }
        } catch(Exception e) {
            throw new RuntimeException("Invalid config_json format", e);
        }

        emailService.sendEmail(to, subject, input);

        return "EMAIL SENT SUCCESSFULLY\n\n" + input;

    }


}
