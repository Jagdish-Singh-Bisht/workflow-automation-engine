package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.repository.ShipmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;




@Component("INPUT")
@RequiredArgsConstructor
public class InputNodeExecutor implements NodeExecutor {

    private final ShipmentRepository shipmentRepository;
    private final ObjectMapper objectMapper;

    @Override
    public String execute(String input, WorkflowNode node, Map<String, Object> context) {

        try {
            if(node.getConfigJson() != null && !node.getConfigJson().isEmpty()) {

                Map<String, Object> config =
                        objectMapper.readValue(node.getConfigJson(), Map.class);

                String type = (String) config.get("type");

                // Controlled extension (NOT generic yet)
                if("DB_FETCH_SHIPMENT".equalsIgnoreCase(type)) {

                    var last12Hours = java.time.LocalDateTime.now().minusHours(12);

                    var shipments = shipmentRepository.findByLastUpdatedAfter(last12Hours);

                    context.put("data", shipments);

                    return "Fetched " + shipments.size() + " shipments";
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return input; // just pass through
    }
}
