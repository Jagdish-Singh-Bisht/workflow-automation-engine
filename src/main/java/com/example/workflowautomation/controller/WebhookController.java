package com.example.workflowautomation.controller;



import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.service.UserCredentialService;
import com.example.workflowautomation.webhook.WebhookHandler;
import com.example.workflowautomation.webhook.WebhookHandlerFactory;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.twilio.security.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.Map;



@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookHandlerFactory factory;
    private final UserRepository userRepository;
    private final UserCredentialService userCredentialService;

    public WebhookController(WebhookHandlerFactory factory,
                             UserRepository userRepository,
                             UserCredentialService userCredentialService) {

        this.factory = factory;
        this.userRepository = userRepository;
        this.userCredentialService = userCredentialService;


    }

    @PostMapping("/{source}")
    public String handle(@PathVariable String source,
                         @RequestParam Map<String, String> params,
                         HttpServletRequest request ) {

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

        String authToken = userCredentialService.getDecryptedCredential(
                user,
                "TWILIO",
                "AUTH_TOKEN"
        );

        String signature = request.getHeader("X-Twilio-Signature");

        RequestValidator validator = new RequestValidator(authToken);

        String url = "https://fourpenny-gricelda-nonsyllogistical.ngrok-free.dev"
                + "/webhook/"
                + source;

        boolean valid = validator.validate(
                url,
                params,
                signature
        );

        if(!valid) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Invalid Twilio signature"
            );
        }


        params.put("userId", user.getId().toString());
        params.put("whatsappNumber", phoneNumber);

        handler.handle(params);

        return "OK";
    }

}
