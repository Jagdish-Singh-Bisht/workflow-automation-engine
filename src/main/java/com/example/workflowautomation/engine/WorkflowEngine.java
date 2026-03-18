package com.example.workflowautomation.engine;


import com.example.workflowautomation.entity.ExecutionLog;
import com.example.workflowautomation.entity.NodeExecutionLog;
import com.example.workflowautomation.repository.ExecutionLogRepository;
import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.entity.Workflow;
import com.example.workflowautomation.repository.NodeExecutionLogRepository;
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
    private final ExecutionLogRepository executionLogRepository;
    private final ExecutorFactory executorFactory;
    private final NodeExecutionLogRepository nodeExecutionLogRepository;



    public String runWorkflow(Long workflowId, String input) {

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        String currentData = input;

        try {

            List<WorkflowNode> nodes =
                    workflowNodeRepository.findByWorkflowOrderBySequenceOrderAsc(workflow);


            for (WorkflowNode node : nodes) {

                NodeExecutor executor =
                        executorFactory.getExecutor(node.getNodeType());

                String inputBeforeNode = currentData;

                currentData = executor.execute(currentData, node);


                NodeExecutionLog log = NodeExecutionLog.builder()
                        .workflow(workflow)
                        .nodeType(node.getNodeType())
                        .status("SUCCESS")
                        .inputData(inputBeforeNode)
                        .outputData(currentData)
                        .executedAt(java.time.LocalDateTime.now())
                        .build();

                nodeExecutionLogRepository.save(log);

            }

            // SAVE SUCCESS LOG
            ExecutionLog log = ExecutionLog.builder()
                    .workflow(workflow)
                    .inputData(input)
                    .outputData(currentData)
                    .status("SUCCESS")
                    .executedAt(java.time.LocalDateTime.now())
                    .build();

            executionLogRepository.save(log);
            return currentData;

        } catch (Exception  e) {

            // SAVE FAILURE LOG
            ExecutionLog log = ExecutionLog.builder()
                    .workflow(workflow)
                    .inputData(input)
                    .outputData(null)
                    .status("FAILURE")
                    .executedAt(java.time.LocalDateTime.now())
                    .build();

            executionLogRepository.save(log);

            throw e;
        }

    }

}
