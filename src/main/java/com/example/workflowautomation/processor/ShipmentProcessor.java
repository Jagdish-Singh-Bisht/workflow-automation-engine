package com.example.workflowautomation.processor;


import com.example.workflowautomation.entity.Shipment;


import java.util.List;




public class ShipmentProcessor {

    public static String generateSummary(List<Shipment> shipments) {

        long total = shipments.size();

        long delivered = shipments.stream()
                .filter(s -> "DELIVERED".equalsIgnoreCase(s.getStatus()))
                .count();

        long pending = shipments.stream()
                .filter(s -> "PENDING".equalsIgnoreCase(s.getStatus()))
                .count();

        List<Shipment> highPriority = shipments.stream()
                .filter(s -> "PENDING".equalsIgnoreCase(s.getStatus()))
                .filter(s -> "HIGH".equalsIgnoreCase(s.getPriority()))
                .toList();


        StringBuilder report = new StringBuilder();

        report.append("Shipment Summary (LastUpdated)\n\n");

        report.append("Total Shipments: ").append(total).append("\n");
        report.append("Delivered: ").append(delivered).append("\n");
        report.append("Pending: ").append(pending).append("\n\n");

        report.append("High Priority Pending:\n");

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

        return report.toString();
    }

}
