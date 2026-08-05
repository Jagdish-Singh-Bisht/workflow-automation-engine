package com.example.workflowautomation.controller;



import com.example.workflowautomation.dto.ChangePasswordRequest;
import com.example.workflowautomation.dto.ChangeUsernameRequest;
import com.example.workflowautomation.service.ProfileService;
import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.service.WorkflowService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final WorkflowService workflowService;
    private final ProfileService profileService;

    @GetMapping
    public String profile(Model model) {

        User currentUser = workflowService.getCurrentUser();

        model.addAttribute("user", currentUser);
        model.addAttribute("page", "profile");

        return "layout";

    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {

        model.addAttribute("changePasswordRequest",
                new ChangePasswordRequest());

        model.addAttribute("page", "change-password");

        return "layout";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute ChangePasswordRequest changePasswordRequest,
                                 RedirectAttributes redirectAttributes) {

        try {
            profileService.changePassword(changePasswordRequest);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Password changed successfully");

        } catch(Exception ex) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    ex.getMessage());

        }

        return "redirect:/profile/change-password";

    }

    @GetMapping("/change-username")
    public String changeUsernamePage(Model model) {

        model.addAttribute("changeUsernameRequest",
                new ChangeUsernameRequest()
        );

        model.addAttribute("page", "change-username");

        return "layout";
    }

    @PostMapping("/change-username")
    public String changeUsername(@ModelAttribute ChangeUsernameRequest request,
                                 RedirectAttributes redirectAttributes) {

        try {
            profileService.changeUsername(request);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "username changed successfully"
            );

        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    ex.getMessage()
            );
        }

        return "redirect:/profile/change-username";
    }

}
