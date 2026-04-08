package com.example.workflowautomation.webhook.handlers;


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


    public WhatsappWebhookHandler(AIService aiService, CalendarService calendarService, WhatsAppService whatsAppService) {
        this.aiService = aiService;
        this.calendarService = calendarService;
        this.whatsAppService = whatsAppService;
    }


    @Override
    public void handle(Map<String, String> data) {

        String message = data.get("Body");

        if(message == null || message.isEmpty()) {
            whatsAppService.sendWhatsapp("Empty message received");
            return;
        }

        System.out.println("Incoming Whatsapp: " + message);

        String intent = aiService.detectIntent(message);

        String reply;

        if("GET_TODAY_SCHEDULE".equals(intent)) {
            reply = calendarService.getTodayEvents();
        } else if("GET_TOMORROW_SCHEDULE".equals(intent)) {
            // reply = calendarService.getTomorrowEvents();
            reply = "feature not implemented yet";
        } else {
            // Gemini AI fallback
            reply = aiService.generateResponse(message);
        }

        whatsAppService.sendWhatsapp(reply);

    }
}
