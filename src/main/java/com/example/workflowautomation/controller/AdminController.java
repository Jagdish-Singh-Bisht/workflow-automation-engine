package com.example.workflowautomation.controller;


import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.service.AdminService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

//    private final UserRepository userRepository;

    private final AdminService adminService;


    @GetMapping("/users")
    public String users(Model model) {

        model.addAttribute("users",
                adminService.getAllUsers());

        model.addAttribute("page", "admin-users");

        return "layout";

    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {

        try {
            adminService.deleteUser(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "User deleted successfully."
            );

        } catch (RuntimeException ex) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    ex.getMessage()
            );
        }

        return "redirect:/admin/users";
    }
}
