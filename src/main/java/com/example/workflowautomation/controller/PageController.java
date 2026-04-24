package com.example.workflowautomation.controller;


import com.example.workflowautomation.service.WorkflowService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;



@Controller
public class PageController {


    private final WorkflowService workflowService;

    public PageController(WorkflowService workflowService) {
        this.workflowService = workflowService;
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
        model.addAttribute("page", "dashboard");
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




}
