package com.example.workflowautomation.webhook.handlers;


import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.ai.AIService;
import com.example.workflowautomation.service.CalendarService;
import com.example.workflowautomation.service.WhatsAppService;

import com.example.workflowautomation.webhook.WebhookHandler;
import org.springframework.stereotype.Component;

import java.util.Map;



@Component("whatsapp")
public class WhatsappWebhookHandler implements WebhookHandler {

    private final AIService aiService;
    private final CalendarService calendarService;
    private final WhatsAppService whatsAppService;
    private final UserRepository userRepository;


    public WhatsappWebhookHandler(AIService aiService,
                                  CalendarService calendarService,
                                  WhatsAppService whatsAppService,
                                  UserRepository userRepository ) {
        this.aiService = aiService;
        this.calendarService = calendarService;
        this.whatsAppService = whatsAppService;
        this.userRepository = userRepository;
    }


    @Override
    public void handle(Map<String, String> data) {

        String userId = data.get("userId");

        User user = userRepository
                .findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found: " + userId)
                );

//        String from = data.get("from");

        String message = data.get("Body");

        String whatsappNumber = data.get("whatsappNumber");

        if(message == null || message.isEmpty()) {
            whatsAppService.sendWhatsapp(
                    user,
                    "whatsapp:" + whatsappNumber,
                    "Empty message received"
            );

            return;
        }

        System.out.println("Incoming WhatsApp: " + message);

        String intent = aiService.detectIntent(message);

        String reply;

        if("GET_TODAY_SCHEDULE".equals(intent)) {
            reply = calendarService.getTodayEvents();

        } else if("GET_TOMORROW_SCHEDULE".equals(intent)) {
            // reply = calendarService.getTomorrowEvents();
            reply = "feature not implemented yet";

        } else {
            // Gemini AI fallback
//            reply = aiService.generateResponse(message);
            reply = "AI response through WhatsApp is temporarily unavailable.";
        }

        whatsAppService.sendWhatsapp(
                user,
                "whatsapp:" + whatsappNumber,
                reply
        );

    }
}
