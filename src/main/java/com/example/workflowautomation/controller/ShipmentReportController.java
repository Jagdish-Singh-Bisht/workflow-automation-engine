package com.example.workflowautomation.controller;



import com.example.workflowautomation.service.ShipmentReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class ShipmentReportController {

    private final ShipmentReportService shipmentReportService;

    public ShipmentReportController(ShipmentReportService shipmentReportService) {
        this.shipmentReportService = shipmentReportService;
    }

    @GetMapping("/report")
    public String getReport() {
        return shipmentReportService.generateReport();
    }
}