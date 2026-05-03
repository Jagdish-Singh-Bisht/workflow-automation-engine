package com.example.workflowautomation.source;


import com.example.workflowautomation.service.ShipmentReportService;
import com.example.workflowautomation.entity.Shipment;
import com.example.workflowautomation.repository.ShipmentRepository;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;




@Component("DB_SHIPMENT")
@RequiredArgsConstructor
public class DBShipmentSourceHandler implements SourceHandler {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentReportService shipmentReportService;

    @Override
    public void fetch(Map<String, Object> context) {

        LocalDateTime last12Hours = LocalDateTime.now().minusHours(12);

        List<Shipment> shipments =
                shipmentRepository.findByLastUpdatedAfter(last12Hours);

        String report = shipmentReportService.generateReport();

        context.put("data", report);
        context.put("dataType", "shipment");
    }
}
