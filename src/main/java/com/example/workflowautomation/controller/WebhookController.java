package com.example.workflowautomation.controller;


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

    public WebhookController(WebhookHandlerFactory factory) {
        this.factory = factory;
    }

    @PostMapping("/{source}")
    public String handle(@PathVariable String source,
                         @RequestParam Map<String, String> params) {

        WebhookHandler handler = factory.getHandler(source);

        if (handler == null) {
            return "Unknown source";
        }

        handler.handle(params);

        return "OK";
    }

}
