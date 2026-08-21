package com.example.workflowautomation.service;


import com.example.workflowautomation.entity.Shipment;
import com.example.workflowautomation.repository.ShipmentRepository;
import com.example.workflowautomation.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



@Service
public class ReportEmailService {

    private final ShipmentReportService shipmentReportService;
    private final EmailService emailService;
    private final StudentRepository studentRepository;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentExcelService shipmentExcelService;


    public ReportEmailService(ShipmentReportService shipmentReportService,
                              EmailService emailService,
                              StudentRepository studentRepository,
                              ShipmentRepository shipmentRepository,
                              ShipmentExcelService shipmentExcelService) {

        this.shipmentReportService = shipmentReportService;
        this.emailService = emailService;
        this.studentRepository = studentRepository;
        this.shipmentRepository = shipmentRepository;
        this.shipmentExcelService = shipmentExcelService;

    }


    public String sendReportToAll() {


        LocalDateTime last24Hours =
                LocalDateTime.now().minusHours(24);

        List<Shipment> fresh =
                shipmentRepository.findByLastUpdatedAfter(last24Hours);

        String report = shipmentReportService.generateReport();

        byte[] excel = shipmentExcelService.generateExcel(fresh);

        List<String> emails = studentRepository.findByActiveTrue()
                .stream()
                .map(s -> s.getEmail())
                .toList();

        return report;

    }
}
