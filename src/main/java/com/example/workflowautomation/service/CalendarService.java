package com.example.workflowautomation.service;


import org.springframework.stereotype.Service;




@Service
public class CalendarService {

    public String getTodayEvents() {
        try {

            var httpTransport = com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport();

            var jsonFactory = com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance();

            var secrets = com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.load(
                    jsonFactory,
                    new java.io.InputStreamReader(new java.io.FileInputStream("google-calendar-credentials.json"))
            );

            var flow = new com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder(
                    httpTransport,
                    jsonFactory,
                    secrets,
                    java.util.Collections.singleton(com.google.api.services.calendar.CalendarScopes.CALENDAR_READONLY)
            ).setAccessType("offline").build();

            var credential = new com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp(
                    flow,
                    new com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver()
            ).authorize("user");

            var service = new com.google.api.services.calendar.Calendar.Builder(
                    httpTransport,
                    jsonFactory,
                    credential
            ).setApplicationName("Workflow").build();


            var now = new com.google.api.client.util.DateTime(System.currentTimeMillis());

            var events = service.events().list("primary")
                    .setMaxResults(5)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            if (events.getItems().isEmpty()) {
                return "No events today";
            }

            StringBuilder res = new StringBuilder("Today's Schedule:\n");

            events.getItems().forEach(e -> {
                res.append("• ").append(e.getSummary()).append("\n");
            });

            return res.toString();

        } catch (Exception e) {
            return "Error fetching calendar";
        }
    }
}