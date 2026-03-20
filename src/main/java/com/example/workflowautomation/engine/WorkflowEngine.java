package com.example.workflowautomation.engine;


import com.example.workflowautomation.dto.WorkflowRunRequest;
import com.example.workflowautomation.entity.Task;
import com.example.workflowautomation.entity.ExecutionLog;
import com.example.workflowautomation.entity.NodeExecutionLog;
import com.example.workflowautomation.repository.*;
import com.example.workflowautomation.entity.WorkflowNode;
import com.example.workflowautomation.entity.Workflow;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class WorkflowEngine {

    private final WorkflowRepository workflowRepository;
    private final WorkflowNodeRepository workflowNodeRepository;
    private final ExecutionLogRepository executionLogRepository;
    private final ExecutorFactory executorFactory;
    private final NodeExecutionLogRepository nodeExecutionLogRepository;
    private final TaskRepository taskRepository;
    private final TemplateRepository templateRepository;


    public String runWorkflow(WorkflowRunRequest request) {

        String input = request.getInput();

        Workflow workflow = workflowRepository.findById(request.getWorkflowId())
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        String currentData = input;

        Map<String, Object> context = new HashMap<>();
        context.put("email", request.getEmail());

        Task task = taskRepository.findById(request.getTaskId())
                        .orElse(null);

        /*
        if(task == null) {
            context.put("taskName", "Default Task");
            context.put("description", "No description");
            context.put("date", "N/A");
        }

         */

        context.put("taskName", task.getTaskName());
        context.put("description", task.getDescription());
        context.put("date", task.getTaskDate());
        context.put("audience", task.getAudience());

        try {

            List<WorkflowNode> nodes =
                    workflowNodeRepository.findByWorkflowOrderBySequenceOrderAsc(workflow);


            for (WorkflowNode node : nodes) {

                NodeExecutor executor =
                        executorFactory.getExecutor(node.getNodeType());

                String inputBeforeNode = currentData;

                currentData = executor.execute(currentData, node, context);


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
