package com.example.workflowautomation.webhook;

import org.springframework.stereotype.Component;
import java.util.Map;



@Component
public class WebhookHandlerFactory {

    private final Map<String, WebhookHandler> handlers;

    public WebhookHandlerFactory(Map<String, WebhookHandler> handlers) {
        this.handlers = handlers;
    }

    public WebhookHandler getHandler(String source) {
        return handlers.get(source.toLowerCase());
    }

}
