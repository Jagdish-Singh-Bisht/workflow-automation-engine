package com.example.workflowautomation.service;



import com.example.workflowautomation.entity.User;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.stereotype.Service;



@Service
public class WhatsAppService {

    private UserCredentialService userCredentialService;

    public WhatsAppService(UserCredentialService userCredentialService) {

        this.userCredentialService = userCredentialService;
    }

    public void sendWhatsapp(User user,
                             String toNumber,
                             String messageText) {

        String accountSid = userCredentialService.getDecryptedCredential(
                user,
                "TWILIO",
                "ACCOUNT_SID"
        );

        String authToken = userCredentialService.getDecryptedCredential(
                user,
                "TWILIO",
                "AUTH_TOKEN"
        );

        Twilio.init(accountSid, authToken);

        Message message = Message.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber("whatsapp:+14155238886"),
                messageText
        ).create();

        System.out.println("Whatsapp message sent with SID: " + message.getSid());
    }
}
