package com.example.workflowautomation.controller;



import com.example.workflowautomation.entity.UserCredential;
import com.example.workflowautomation.service.UserCredentialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import java.util.List;



@Controller
@RequestMapping("/profile/credentials")
@RequiredArgsConstructor
public class ProfileCredentialController {

    private final UserCredentialService userCredentialService;

    @GetMapping
    public String credentials(Model model) {

        List<UserCredential> credentials =
                userCredentialService.getCurrentUserCredentials();

        model.addAttribute("credentials", credentials);
        model.addAttribute("page", "credentials");

        return "layout";

    }



}
