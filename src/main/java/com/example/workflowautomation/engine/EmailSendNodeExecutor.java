package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.service.EmailService;
import org.springframework.stereotype.Component;



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

        emailService.sendEmail(to, subject, input);

        return "EMAIL SENT SUCCESSFULLY\n\n" + input;

    }


}
