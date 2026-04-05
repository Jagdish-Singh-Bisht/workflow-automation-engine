package com.example.workflowautomation.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;



@Service
public class WhatsAppService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    @Value("${twilio.whatsapp.to}")
    private String toNumber;



    public void sendWhatsapp(String messageText) {

        Twilio.init(accountSid, authToken);

        Message message = Message.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber(fromNumber),
                messageText
        ).create();

        System.out.println("Whatsapp message sent with SID: " + message.getSid());
    }
}
