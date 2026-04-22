package com.example.workflowautomation.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;



@Controller
public class PageController {

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
        return "layout";
    }

    @GetMapping("/triggers")
    public String triggers(Model model) {
        model.addAttribute("page", "triggers");
        return "layout";
    }


}
