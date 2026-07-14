package com.example.workflowautomation.controller;


import com.example.workflowautomation.exception.UsernameAlreadyExistsException;
import com.example.workflowautomation.service.UserService;
import com.example.workflowautomation.dto.RegisterRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.ui.Model;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute(
                "registerRequest",
                new RegisterRequest()
        );

        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                           BindingResult bindingResult,
                           Model model) {

        System.out.println("Register endpoint called");

        if(bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(request);
            return "redirect:/login";

        } catch (UsernameAlreadyExistsException e) {

            model.addAttribute("error", e.getMessage());
            return "register";
        }

//        return "redirect:/login";
    }

}
