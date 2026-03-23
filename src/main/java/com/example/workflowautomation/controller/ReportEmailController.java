package com.example.workflowautomation.controller;


import com.example.workflowautomation.service.ReportEmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class ReportEmailController {

    private final ReportEmailService reportEmailService;

    public ReportEmailController(ReportEmailService reportEmailService) {
        this.reportEmailService = reportEmailService;
    }

    @GetMapping("/send-report")
    public String sendReport() {
        return reportEmailService.sendReportToAll();
    }
}