package com.example.workflowautomation.controller;



import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.webhook.WebhookHandler;
import com.example.workflowautomation.webhook.WebhookHandlerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;



@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookHandlerFactory factory;
    private final UserRepository userRepository;

    public WebhookController(WebhookHandlerFactory factory,
                             UserRepository userRepository) {

        this.factory = factory;
        this.userRepository = userRepository;

    }

    @PostMapping("/{source}")
    public String handle(@PathVariable String source,
                         @RequestParam Map<String, String> params) {

        WebhookHandler handler = factory.getHandler(source);

        if (handler == null) {
            return "Unknown source";
        }

        String from = params.get("From");

        String phoneNumber = from != null && from.startsWith("whatsapp:")
                ? from.substring("whatsapp:".length())
                : from;

        User user = userRepository
                .findByWhatsappNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException(
                        "No user found for WhatsApp number: " + phoneNumber
                ));

        params.put("userId", user.getId().toString());
        params.put("whatsappNumber", phoneNumber);

        handler.handle(params);

        return "OK";
    }

}
