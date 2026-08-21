package com.example.workflowautomation.service;

import com.example.workflowautomation.entity.Shipment;
import com.example.workflowautomation.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;




@Service
public class ShipmentReportService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentReportService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public String generateReport() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last24Hours = now.minusHours(24);

        List<Shipment> recent =
                shipmentRepository.findByLastUpdatedAfter(last24Hours);


        if(recent.isEmpty()) {
            return "Subject: Shipment Report\n\nNo shipment updates in the last 24 hours.";
        }


        long totalUpdated = recent.size();

        long delivered = recent.stream()
                .filter(s -> "DELIVERED".equalsIgnoreCase(s.getStatus()))
                .count();

        long pending = recent.stream()
                .filter(s -> "PENDING".equalsIgnoreCase(s.getStatus()))
                .count();


        List<Shipment> highPriority = recent.stream()
                .filter(s -> "PENDING".equalsIgnoreCase(s.getStatus()))
                .filter(s -> "HIGH".equalsIgnoreCase(s.getPriority()))
                .toList();

        int totalQuantity = recent.stream()
                .mapToInt(Shipment::getQuantity)
                .sum();


        StringBuilder report = new StringBuilder();

        report.append("Subject: Shipment Report (Last 24 Hours)\n\n");
        report.append("Dear Team,\n\n");

        report.append("Report Time Range:\n");
        report.append("From: ").append(last24Hours).append("\n");
        report.append("To: ").append(now).append("\n\n");

        report.append("Today's Updates:\n");
        report.append("Total Updated Shipments: ").append(totalUpdated).append("\n");
        report.append("Delivered: ").append(delivered).append("\n");
        report.append("Pending: ").append(pending).append("\n\n");

        report.append("High Priority Pending: \n");

        if(highPriority.isEmpty()) {
            report.append("None\n");
        } else {
            for(Shipment s : highPriority) {
                report.append("- ")
                        .append(s.getShipmentId())
                        .append(" (")
                        .append(s.getClientName())
                        .append(")\n");
            }
        }

        report.append("\nTotal Quantity Processed: ")
                .append(totalQuantity)
                .append(" units\n\n");

        if(pending == 0) {
            report.append("All shipments delivered successfully.\n\n");
        } else {
            report.append("Operations are running with some pending deliveries.\n\n");
        }

        report.append("Regards,\n");
        report.append("Automation System");

        return report.toString();
    }
}
