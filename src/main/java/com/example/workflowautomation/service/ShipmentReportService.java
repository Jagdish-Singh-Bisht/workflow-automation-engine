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

        // Last 24hrs data
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last24Hours = now.minusHours(1);

        List<Shipment> recent =
                shipmentRepository.findByLastUpdatedAfter(last24Hours);


        // handle no data case
        if(recent.isEmpty()) {
            return "Subject: Shipment Report\n\nNo shipment updates in the last 24 hours.";
        }


        // Counts
        long totalUpdated = recent.size();

        long delivered = recent.stream()
                .filter(s -> "DELIVERED".equalsIgnoreCase(s.getStatus()))
                .count();

        long pending = recent.stream()
                .filter(s -> "PENDING".equalsIgnoreCase(s.getStatus()))
                .count();


        // High Priority Pending
        List<Shipment> highPriority = recent.stream()
                .filter(s -> "PENDING".equalsIgnoreCase(s.getStatus()))
                .filter(s -> "HIGH".equalsIgnoreCase(s.getPriority()))
                .toList();

        // Total Quantity
        int totalQuantity = recent.stream()
                .mapToInt(Shipment::getQuantity)
                .sum();


        // Build Report
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

        // Dynamic Message
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
