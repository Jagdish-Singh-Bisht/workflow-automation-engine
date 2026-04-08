package com.example.workflowautomation.service;


import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.Event;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;





@Service
public class CalendarService {

    public String getTodayEvents() {
        try {

            var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            var jsonFactory = JacksonFactory.getDefaultInstance();

            GoogleClientSecrets secrets = GoogleClientSecrets.load(
                    jsonFactory,
                    new InputStreamReader(new FileInputStream("google-calendar-credentials.json"))
            );

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport,
                    jsonFactory,
                    secrets,
                    Collections.singleton(CalendarScopes.CALENDAR_READONLY))
                    .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                    .setAccessType("offline")
                    .build();

            var credential = new AuthorizationCodeInstalledApp(
                    flow,
                    new LocalServerReceiver()
            ).authorize("user");

            Calendar service = new Calendar.Builder(
                    httpTransport,
                    jsonFactory,
                    credential
            ).setApplicationName("Workflow").build();

            // ✅ TODAY RANGE
            var todayStart = java.time.LocalDate.now()
                    .atStartOfDay(ZoneId.systemDefault());

            var todayEnd = java.time.LocalDate.now()
                    .plusDays(1)
                    .atStartOfDay(ZoneId.systemDefault());

            DateTime startOfDay = new DateTime(Date.from(todayStart.toInstant()));
            DateTime endOfDay = new DateTime(Date.from(todayEnd.toInstant()));

            Events events = service.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(startOfDay)
                    .setTimeMax(endOfDay)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            if (events.getItems().isEmpty()) {
                return "No events today";
            }

            StringBuilder res = new StringBuilder("Today's Schedule:\n\n");

            int count = 1;

            for (Event e : events.getItems()) {

                String summary = e.getSummary();
                if (summary == null) continue;

                String lower = summary.toLowerCase();
                if (lower.contains("birthday") || lower.contains("holiday")) continue;

                DateTime start = e.getStart().getDateTime();
                DateTime end = e.getEnd().getDateTime();

                if (start == null) start = e.getStart().getDate();
                if (end == null) end = e.getEnd().getDate();

                Instant startInstant = Instant.ofEpochMilli(start.getValue());
                Instant endInstant = Instant.ofEpochMilli(end.getValue());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a")
                        .withZone(ZoneId.systemDefault());

                String startTime = formatter.format(startInstant);
                String endTime = formatter.format(endInstant);

                String link = e.getHtmlLink();
                String meetLink = e.getHangoutLink();

                List<String> customLinks = new ArrayList<>();

                String description = e.getDescription();
                System.out.println("Event description: " + description);

                if (description != null) {

                    Pattern pattern = Pattern.compile("https?://[^\\s]+");
                    Matcher matcher = pattern.matcher(description);

                    while (matcher.find()) {
                        customLinks.add(matcher.group());
                    }

//                    Pattern pattern = Pattern.compile("(https?://\\S+)");
//                    Matcher matcher = pattern.matcher(description);
//
//                    while(matcher.find()) {
//                        customLinks.add(matcher.group(1));
//                    }
                }

                /*
                if(description != null && description.contains("http")) {
                    int linkStart = description.indexOf("http");
                    int linkEnd = description.indexOf(" ", linkStart);

                    if(linkEnd == -1) linkEnd = description.length();

                    customLink = description.substring(linkStart, linkEnd);
                }

                 */

                res.append(count++).append(". ").append(summary).append("\n")
                        .append("   ⏰ ").append(startTime).append(" - ").append(endTime).append("\n");

                if(!customLinks.isEmpty()) {
                    for(String cl: customLinks) {
                        res.append("   🔗 Link: ").append(cl).append("\n");
                    }
                }
                else if(meetLink != null) {
                    res.append("   🎥 Meet: ").append(meetLink).append("\n");
                }
                else if(link != null) {
                    res.append("   🔗 Event: ").append(link).append("\n");
                }

                res.append("\n");
            }

            return res.toString();

        } catch (Exception e) {
            return "Error fetching calendar";
        }
    }
}