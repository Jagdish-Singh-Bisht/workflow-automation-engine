package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.dto.EmailDraft;
import org.springframework.stereotype.Component;

import java.util.Map;




@Component("EMAIL_DRAFT")
public class EmailDraftNodeExecutor implements NodeExecutor {


    @Override
    public String execute(String input, WorkflowNode node, Map<String , Object> context) {

        String to = "jbisht526@mail.com";
        String subject = "Automated Notice";

        EmailDraft draft = new EmailDraft(to, subject, input);

//        return "EMAIL DRAFT\n\n"
//                + "To: " + draft.getTo()
//                + "\nSubject: " + draft.getSubject()
//                + "\n\n"
//                + draft.getBody();

        return draft.getBody();
    }


}
