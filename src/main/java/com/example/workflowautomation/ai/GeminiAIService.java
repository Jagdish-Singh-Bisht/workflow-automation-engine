package com.example.workflowautomation.ai;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;

import java.util.List;
import java.util.Map;


@Service
public class GeminiAIService implements AIService {


    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public String generateResponse(String input) {

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        Map<String, Object> request = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", input)
                                )
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(request, headers);



        Map response = restTemplate.postForObject(url, entity, Map.class);

        if(response == null) {
            throw new RuntimeException("AI response is null");
        }


        List candidates = (List) response.get("candidates");

        if(candidates == null || candidates.isEmpty()) {
            throw new RuntimeException("No candidates in AI response: " + response);
        }


        Map firstCandidate = (Map) candidates.get(0);

        Map content = (Map) firstCandidate.get("content");

        if(content == null) {
            throw new RuntimeException("Content is null: " + response);
        }


        List parts = (List) content.get("parts");

        if(parts == null || parts.isEmpty()) {
            throw new RuntimeException("Parts are null/empty: " + response);
        }


        Map part = (Map) parts.get(0);
        String text = (String) part.get("text");

        if(text == null || text.isBlank()) {
            throw new RuntimeException("AI returned empty text: " + response);
        }

        return text;
    }


    @Override
    public String detectIntent(String input) {

        if(input == null) return "UNKNOWN";

        String lower = input.toLowerCase();

        if(lower.contains("schedule") || lower.contains("today") || lower.contains("events") || lower.contains("calendar")) {
            return "GET_TODAY_SCHEDULE";
        }

        if(lower.contains("tomorrow") || lower.contains("next day") || lower.contains("upcoming")) {
            return "GET_TOMORROW_SCHEDULE";
        }

        return "UNKNOWN";
    }

}
