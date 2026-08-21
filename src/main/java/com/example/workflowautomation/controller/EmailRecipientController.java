package com.example.workflowautomation.controller;



import com.example.workflowautomation.service.EmailRecipientService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class EmailRecipientController {

    private final EmailRecipientService emailRecipientService;

    @GetMapping("/recipients")
    public String recipients(Model model) {

        model.addAttribute(
                "recipients",
                emailRecipientService.getCurrentUserRecipients()
        );

        model.addAttribute("page", "email-recipients");

        return "layout";

    }

    @PostMapping("/recipients")
    public String addRecipient(@RequestParam String name,
                               @RequestParam String email ) {

        emailRecipientService.addRecipient(name, email);

        return "redirect:/recipients";

    }

    @PostMapping("/recipients/{id}/active")
    public String setActive(@PathVariable Long id,
                            @RequestParam boolean active ) {

        emailRecipientService.setActive(id, active);

        return "redirect:/recipients";

    }

    @PostMapping("/recipients/{id}/delete")
    public String deleteRecipient(@PathVariable Long id) {

        emailRecipientService.deleteRecipient(id);

        return "redirect:/recipients";

    }


}
