package com.example.workflowautomation.service;


import com.example.workflowautomation.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ReportEmailService {

    private final ShipmentReportService shipmentReportService;
    private final EmailService emailService;
    private final StudentRepository studentRepository;


    public ReportEmailService(ShipmentReportService shipmentReportService,
                              EmailService emailService,
                              StudentRepository studentRepository) {

        this.shipmentReportService = shipmentReportService;
        this.emailService = emailService;
        this.studentRepository = studentRepository;

    }


    public String sendReportToAll() {

        String report = shipmentReportService.generateReport();

        List<String> emails = studentRepository.findAll()
                .stream()
                .map(s -> s.getEmail())
                .toList();


        String subject = "Daily Shipment Report";

        for(String email : emails) {
            System.out.println("Sending report to: " + email);
            emailService.sendEmail(email, subject, report);
        }

        return "Report sent to all users!";

    }
}
