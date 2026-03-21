package com.example.workflowautomation.engine;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.service.EmailService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;




@Component("EMAIL_SEND")
public class EmailSendNodeExecutor implements NodeExecutor{

    private final EmailService emailService;

    public EmailSendNodeExecutor(EmailService emailService) {
        this.emailService = emailService;
    }


    @Override
    public String execute(String input, WorkflowNode node, Map<String, Object> context) {

//        String to = (String) context.get("email");
//        if (to == null) {
//            to = "jbisht526@gmail.com";
//        }

        Object emailsObj = context.get("emails");

        List<String> emails;

        if(emailsObj != null) {
            emails = (List<String>) emailsObj;
        } else {
            // fallback (single email)
            String singleEmail = (String) context.get("email");
            if(singleEmail == null) {
                singleEmail = "jbisht526@gmail.com";
            }

            emails = List.of(singleEmail);
        }



        String taskName = (String) context.get("taskName");

        if(taskName == null) {
            taskName = "Task";
        }

        String subject = taskName + " - Notification";


        try{

            if(node.getConfigJson() != null) {
                ObjectMapper mapper = new ObjectMapper();

                Map<String, String> config =
                        mapper.readValue(node.getConfigJson(), Map.class);

//                if(config.containsKey("to")) {
//                    emails = List.of(config.get("to"));
//                }

                if(config.containsKey("subject")) {
                    subject = config.get("subject");
                }
            }
        } catch(Exception e) {
            throw new RuntimeException("Invalid config_json format", e);
        }

        System.out.println("Emails list: " + emails);

        for(String to : emails) {
            System.out.println("Sending to: " + to);
            emailService.sendEmail(to, subject, input);
        }

        return "EMAIL SENT SUCCESSFULLY\n\n" + input;

    }


}
