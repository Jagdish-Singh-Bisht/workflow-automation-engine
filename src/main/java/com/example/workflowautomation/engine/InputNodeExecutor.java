package com.example.workflowautomation.engine;


import com.example.workflowautomation.source.SourceHandler;
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
    private final Map<String, SourceHandler> sourceHandler;
    private final ObjectMapper objectMapper;

    @Override
    public String execute(String input, WorkflowNode node, Map<String, Object> context) {

        try {
            if(node.getConfigJson() != null && !node.getConfigJson().isEmpty()) {

                Map<String, Object> config =
                        objectMapper.readValue(node.getConfigJson(), Map.class);

                String type = (String) config.get("type");

                SourceHandler handler = sourceHandler.get(type);

                if(handler != null) {
                    handler.fetch(context);

                    return (String) context.get("data");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return input;
    }
}
