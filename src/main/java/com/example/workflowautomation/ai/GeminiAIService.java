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

//        return response.toString();

        List candidates = (List) response.get("candidates");

        Map firstCandidate = (Map) candidates.get(0);
        Map content = (Map) firstCandidate.get("content");

        List parts = (List) content.get("parts");

        Map part = (Map) parts.get(0);

        return (String) part.get("text");
    }

}
