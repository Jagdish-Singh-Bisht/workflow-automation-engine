package com.example.workflowautomation.source;


import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.BodyPart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;





@Component("EMAIL")
public class EmailSourceHandler implements SourceHandler {

    @Value("${mail.imap.host}")
    private String host;

    @Value("${mail.imap.port}")
    private String port;

    @Value("${MAIL_USERNAME}")
    private String username;

    @Value("${MAIL_APP_PASSWORD}")
    private String password;

    @Override
    public void fetch(Map<String, Object> context) {

        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props);

            Store store = session.getStore("imaps");
            store.connect(host, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();

            if (messages.length == 0) {
                context.put("data", "No emails found");
                context.put("dataType", "email");
                return;
            }

            // Get latest email
            for(int i = messages.length - 1; i >= 0; i--) {
                Message message = messages[i];
                String subject = message.getSubject();

                if(subject != null) {
                    String lower = subject.toLowerCase();

                    // Filter
                    if(lower.contains("internship") ||
                    lower.contains("placement") ||
                    lower.contains("exam") ) {

                        String content = extractText(message);

                        String result = "SUBJECT: " + subject + "\n\n" + content;

                        context.put("data", result);
                        context.put("dataType", "email");

                        System.out.println("Important email found: " + subject);

                        inbox.close(false);
                        store.close();

                        return; // Exit after processing the latest relevant email
                    }
                }
            }

            // If no relevant email found
            context.put("data", "No relevant emails found");
            context.put("dataType", "email");

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
            context.put("data", "Error reading email");
            context.put("dataType", "email");
        }
    }


    // Handle multipart emails
    private String extractText(Message message) throws Exception {

        Object content = message.getContent();

        // Plain text
        if(content instanceof String) {
            return (String) content;
        }

        // Multipart (HTML email)
        if(content instanceof Multipart) {
            Multipart multipart = (Multipart) content;

            for(int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);

                if(bodyPart.isMimeType("text/plain")) {
                    return bodyPart.getContent().toString();
                }
            }
        }

        return "No readable content found";

    }
}