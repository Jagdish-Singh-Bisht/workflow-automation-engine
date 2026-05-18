package com.example.workflowautomation.controller;


import com.example.workflowautomation.entity.Workflow;
import com.example.workflowautomation.entity.ExecutionLog;
import com.example.workflowautomation.repository.WorkflowRepository;
import com.example.workflowautomation.repository.ExecutionLogRepository;
import com.example.workflowautomation.service.WorkflowService;
import com.example.workflowautomation.repository.WorkflowTriggerRepository;
import com.example.workflowautomation.entity.WorkflowTrigger;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import java.util.List;




@Controller
public class PageController {


    private final WorkflowService workflowService;
    private final WorkflowTriggerRepository workflowTriggerRepository;
    private final WorkflowRepository workflowRepository;
    private final ExecutionLogRepository executionLogRepository;



    public PageController(WorkflowService workflowService,
                          WorkflowTriggerRepository workflowTriggerRepository,
                          WorkflowRepository workflowRepository,
                          ExecutionLogRepository executionLogRepository) {

        this.workflowService = workflowService;
        this.workflowTriggerRepository = workflowTriggerRepository;
        this.workflowRepository = workflowRepository;
        this.executionLogRepository = executionLogRepository;
    }


    @GetMapping("/test")
    public String test() {
        return "dashboard";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long totalWorkflows = workflowRepository.count();

        long activeTriggers = workflowTriggerRepository.countByIsActive(true);

        long totalExecutions = executionLogRepository.count();

        ExecutionLog lastExecution = executionLogRepository.findTopByOrderByExecutedAtDesc();

        List<ExecutionLog> recentExecutions = executionLogRepository
                .findTop5ByOrderByExecutedAtDesc();

        long successfulExecutions = executionLogRepository
                .countByStatus("SUCCESS");

        long failedExecutions = executionLogRepository.countByStatus("FAILURE");


        model.addAttribute("page", "dashboard");

        model.addAttribute("totalWorkflows", totalWorkflows);
        model.addAttribute("activeTriggers", activeTriggers);
        model.addAttribute("totalExecutions", totalExecutions);

        model.addAttribute(
                "lastExecutionTime",
                lastExecution != null ? lastExecution.getExecutedAt() : "--"
        );

        model.addAttribute("recentExecutions", recentExecutions);

        model.addAttribute("successfulExecutions", successfulExecutions);
        model.addAttribute("failedExecutions", failedExecutions);

        return "layout";
    }


    @GetMapping("/workflows")
    public String workflows(Model model) {
        model.addAttribute("page", "workflows");

        model.addAttribute("workflows", workflowService.getAllWorkflows());

        return "layout";
    }

    @GetMapping("/triggers")
    public String triggers(Model model) {
        model.addAttribute("page", "triggers");

        return "layout";
    }




    @GetMapping("/workflows/create")
    public String createWorkflowPage(Model model) {
        model.addAttribute("page", "create-workflow");

        return "layout";
    }

    @PostMapping("/workflows/create")
    public String createWorkflow(@RequestParam String name) {

        // For now hardcode user
        workflowService.createWorkflow("ajju", name);

        return "redirect:/workflows";
    }




    @GetMapping("/workflows/{id}/nodes")
    public String workflowNodes(@PathVariable Long id, Model model) {

        model.addAttribute("page", "workflow-nodes");
        model.addAttribute("workflowId", id);

        // get nodes from service
        model.addAttribute("nodes", workflowService.getOrderedNodes(id));

        WorkflowTrigger trigger = workflowTriggerRepository.findAll()
                        .stream()
                                .filter(t -> t.getWorkflowId().equals(id))
                                        .findFirst()
                                                .orElse(null);

        model.addAttribute("trigger", trigger);

        System.out.println("Workflow ID: " + id);

        return "layout";
    }


    /*
    @PostMapping("/workflows/{id}/nodes")
    public String workflowNodes(@PathVariable Long id,
                                @RequestParam String nodeType,
                                @RequestParam Integer sequenceOrder) {

        workflowService.addNode(id, nodeType, sequenceOrder);

        return "redirect:/workflows/" + id + "/nodes";
    }


     */



    @PostMapping("/workflows/{id}/nodes")
    public String addNode(@PathVariable Long id,
                          @RequestParam String nodeType,
                          @RequestParam Integer sequenceOrder,
                          @RequestParam(required = false) String configJson) {

        workflowService.addNode(id, nodeType, sequenceOrder, configJson);

        return "redirect:/workflows/" + id + "/nodes";

    }


    @PostMapping("/workflows/{id}/trigger")
    public String saveTrigger(
            @PathVariable Long id,
            @RequestParam String cronExpression,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Boolean emailEnabled,
            @RequestParam(required= false) Boolean whatsappEnabled) {

        WorkflowTrigger trigger =
                workflowTriggerRepository.findByWorkflowId(id)
                        .orElse(
                                WorkflowTrigger.builder()
                                        .workflowId(id)
                                        .triggerType("CRON")
                                        .build()
                        );

        trigger.setCronExpression(cronExpression);
        trigger.setActive(active != null);
        trigger.setEmailEnabled(emailEnabled != null);
        trigger.setWhatsappEnabled(whatsappEnabled != null);

        workflowTriggerRepository.save(trigger);

        return "redirect:/workflows/" + id + "/nodes";
    }



    @GetMapping("/executions")
    public String executions(Model model) {

        model.addAttribute("page", "executions");

        List<ExecutionLog> logs =
                executionLogRepository.findTop20ByOrderByExecutedAtDesc();

        model.addAttribute("logs", logs);

        return "layout";
    }



    // Workflow detail page
    @GetMapping("/workflows/{id}")
    public String workflowDetails(@PathVariable Long id, Model model) {

        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        WorkflowTrigger trigger = workflowTriggerRepository.findByWorkflowId(id)
                .orElse(null);

        List<ExecutionLog> logs = executionLogRepository
                .findByWorkflowIdOrderByExecutedAtDesc(id);


        model.addAttribute("page", "workflow-details");
        model.addAttribute("workflow", workflow);
        model.addAttribute("trigger", trigger);
        model.addAttribute("totalExecutions", logs.size());

        model.addAttribute(
                "lastExecutiion",
                logs.isEmpty() ? null : logs.get(0)
        );

        model.addAttribute(
                "recentLogs",
                logs.stream().limit(10).toList()
        );

        return "layout";
    }



    // ExecutionDetailsPage

    @GetMapping("/executions/{id}")
    public String executionDetails(@PathVariable Long id,
                                   Model model) {

        ExecutionLog log = executionLogRepository.findById(id)
                .orElseThrow();

        model.addAttribute("page", "execution-details");
        model.addAttribute("log", log);

        return "layout";

    }


    @GetMapping("/workflows/{id}/executions")
    public String viewExecutions(@PathVariable Long id, Model model) {

        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow();

        List<ExecutionLog> logs = executionLogRepository
                .findByWorkflowIdOrderByExecutedAtDesc(id);

        model.addAttribute("page", "executions");
        model.addAttribute("logs", logs);
        model.addAttribute("workflow", workflow);


        return "layout";
    }



}
