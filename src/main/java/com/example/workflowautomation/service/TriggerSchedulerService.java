package com.example.workflowautomation.service;



import com.example.workflowautomation.dto.WorkflowRunRequest;
import com.example.workflowautomation.entity.WorkflowTrigger;
import com.example.workflowautomation.repository.WorkflowTriggerRepository;
import com.example.workflowautomation.engine.WorkflowEngine;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;




@Service
@RequiredArgsConstructor
public class TriggerSchedulerService {

    private final WorkflowTriggerRepository workflowTriggerRepository;
    private final WorkflowEngine workflowEngine;

    @Scheduled(fixedRate = 60000) // runs every seconds
    public void checkTriggers() {

        List<WorkflowTrigger> triggers =
                workflowTriggerRepository.findByTriggerTypeAndIsActive("CRON", true);

        LocalDateTime now = LocalDateTime.now();

        for(WorkflowTrigger trigger : triggers) {

            String cronExpression = trigger.getCronExpression();
            CronExpression cron = CronExpression.parse(cronExpression);

            LocalDateTime nextExecution = cron.next(now.minusMinutes(1));

            if(nextExecution != null &&
            nextExecution.getMinute() == now.getMinute() &&
            nextExecution.getHour() == now.getHour()) {

                Long workflowId = trigger.getWorkflowId();
                System.out.println("Running workflow by CRON: " + workflowId);

                WorkflowRunRequest request = new WorkflowRunRequest();
                request.setWorkflowId(workflowId);
                request.setInput("Scheduled execution");
                workflowEngine.runWorkflow(request);
            }
        }
    }
}
