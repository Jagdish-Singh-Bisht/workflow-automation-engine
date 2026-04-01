package com.example.workflowautomation.source;


import org.springframework.stereotype.Component;
import java.util.Map;



@Component("EMAIL")
public class EmailSourceHandler implements SourceHandler {

    @Override
    public void fetch(Map<String, Object> context) {

        // TODO: fetch emails (IMAP)
        System.out.println("Reading emails ...");

        // dummy data for now (testing)
        String message = "New Internship Opportunity from Amazon";

        context.put("data", message);
        context.put("dataType", "email");
    }

}
