package com.example.workflowautomation.service;



import com.example.workflowautomation.service.UserCredentialService;
import com.example.workflowautomation.entity.User;

import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;





@Service
public class EmailService {

//    private final JavaMailSender mailSender;
    private final UserCredentialService userCredentialService;

    public EmailService(UserCredentialService userCredentialService) {
        this.userCredentialService = userCredentialService;
    }

    private JavaMailSenderImpl createMailSender(User user) {

        String username = userCredentialService.getDecryptedCredential(
                user,
                "EMAIL",
                "USERNAME"
        );

        String appPassword =
                userCredentialService.getDecryptedCredential(
                        user,
                        "EMAIL",
                        "APP_PASSWORD"
                );

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(username);
        mailSender.setPassword(appPassword);

        Properties properties = mailSender.getJavaMailProperties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        return mailSender;

    }


    public void sendEmail(User user,
                          String to,
                          String subject,
                          String body ) {

        JavaMailSenderImpl mailSender = createMailSender(user);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

    }

    public void sendEmailWithAttachment(User user,
                                        String to,
                                        String subject,
                                        String text,
                                        byte[] fileData) {

        try {

            JavaMailSenderImpl mailSender = createMailSender(user);

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            helper.addAttachment("shipment_report.xlsx",
                    new ByteArrayResource(fileData));

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
