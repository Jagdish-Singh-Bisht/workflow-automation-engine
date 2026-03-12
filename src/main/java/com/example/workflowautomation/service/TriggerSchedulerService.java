package com.example.workflowautomation.service;



import com.example.workflowautomation.entity.WorkflowTrigger;
import com.example.workflowautomation.repository.WorkflowTriggerRepository;
import com.example.workflowautomation.engine.WorkflowEngine;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

import java.util.List;






@Service
@RequiredArgsConstructor
public class TriggerSchedulerService {

    private final WorkflowTriggerRepository workflowTriggerRepository;
    private final WorkflowEngine workflowEngine;

    @Scheduled(fixedRate = 60000) // runs every seconds
    public void checkTriggers() {

        List<WorkflowTrigger> triggers =
                workflowTriggerRepository.findByTriggerType("CRON");

        for(WorkflowTrigger trigger : triggers) {
            Long workflowId = trigger.getWorkflowId();
            System.out.println("Running scheduled workflow: " + workflowId);

            workflowEngine.runWorkflow(workflowId, "Triggered execution");
        }
    }
}
