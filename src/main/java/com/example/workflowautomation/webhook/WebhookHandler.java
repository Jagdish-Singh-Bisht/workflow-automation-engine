package com.example.workflowautomation.webhook;

import java.util.Map;


public interface WebhookHandler {

    void handle(Map<String, String> data);

}
