package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.entity.Workflow;
import com.example.workflowautomation.repository.WorkflowRepository;
import com.example.workflowautomation.repository.WorkflowNodeRepository;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Component
@RequiredArgsConstructor
public class WorkflowEngine {

    private final WorkflowRepository workflowRepository;
    private final WorkflowNodeRepository workflowNodeRepository;


    public String runWorkflow(Long workflowId, String input) {

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        List<WorkflowNode> nodes =
                workflowNodeRepository.findByWorkflowOrderBySequenceOrderAsc(workflow);

        String currentData = input;

        for(WorkflowNode node : nodes) {

            switch (node.getNodeType()) {

                case "INPUT":
                    // Do nothing, just pass input
                    break;

                case "AI":
                    // For now simulate AI
                    currentData = "[AI PROCESSED]: " + currentData;
                    break;

                case "OUTPUT":
                    // Final output formatting
                    currentData = "[FINAL OUTPUT]: " + currentData;
                    break;

                default:
                    throw new RuntimeException("Unknown node type");
            }
        }

        return currentData;
    }

}
