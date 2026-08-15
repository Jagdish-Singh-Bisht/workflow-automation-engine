package com.example.workflowautomation.controller;



import com.example.workflowautomation.entity.UserCredential;
import com.example.workflowautomation.service.UserCredentialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

        boolean geminiConfigured =
                userCredentialService.hasCredential("GEMINI");

        boolean emailConfigured =
                userCredentialService.hasEmailCredential();

        boolean twilioConfigured =
                userCredentialService.hasTwilioCredential();

        model.addAttribute("credentials", credentials);
        model.addAttribute("geminiConfigured", geminiConfigured);
        model.addAttribute("emailConfigured", emailConfigured);
        model.addAttribute("twilioConfigured", twilioConfigured);
        model.addAttribute("page", "credentials");

        return "layout";

    }


    @GetMapping("/gemini")
    public String gemini(Model model) {

        boolean geminiConfigured =
                userCredentialService.hasCredential("GEMINI");

        model.addAttribute("geminiConfigured", geminiConfigured);

        return "gemini-credentials";

    }

    @PostMapping("/gemini")
    public String saveGemini(@RequestParam String credentialValue,
                             RedirectAttributes redirectAttributes) {

        userCredentialService.saveCredential(
                "GEMINI",
                "API_KEY",
                credentialValue
        );

        redirectAttributes.addFlashAttribute(
                "success",
                "Gemini API Key saved successfully."
        );

        return "redirect:/profile/credentials";

    }

    @PostMapping("/gemini/delete")
    public String deleteGemini(RedirectAttributes redirectAttributes) {

        userCredentialService.deleteCredential("GEMINI");

        redirectAttributes.addFlashAttribute(
                "success",
                "Gemini credentials removed successfully."
        );

        return "redirect:/profile/credentials";

    }

    @GetMapping("/email")
    public String email(Model model) {

        boolean emailConfigured =
                userCredentialService.hasEmailCredential();

        model.addAttribute("emailConfigured", emailConfigured);

        return "email-credentials";
    }

    @PostMapping("/email")
    public String saveEmail(@RequestParam String emailUsername,
                            @RequestParam String appPassword,
                            RedirectAttributes redirectAttributes) {

        userCredentialService.saveCredential(
                "EMAIL",
                "USERNAME",
                emailUsername
        );

        userCredentialService.saveCredential(
                "EMAIL",
                "APP_PASSWORD",
                appPassword
        );

        redirectAttributes.addFlashAttribute(
                "success",
                "Email credentials saved successfully."
        );

        return "redirect:/profile/credentials";

    }

    @PostMapping("/email/delete")
    public String deleteEmail(RedirectAttributes redirectAttributes) {

        userCredentialService.deleteEmailCredentials();

        redirectAttributes.addFlashAttribute(
                "success",
                "Email credentials deleted successfully."
        );

        return "redirect:/profile/credentials";

    }

    @GetMapping("/whatsapp")
    public String whatsapp(Model model) {

        boolean twilioConfigured =
                userCredentialService.hasTwilioCredential();

        model.addAttribute("twilioConfigured", twilioConfigured);

        return "whatsapp-credentials";

    }


    @PostMapping("/whatsapp")
    public String saveWhatsapp(
            @RequestParam String accountSid,
            @RequestParam String authToken,
            RedirectAttributes redirectAttributes) {

        userCredentialService.saveCredential(
                "TWILIO",
                "ACCOUNT_SID",
                accountSid
        );

        userCredentialService.saveCredential(
                "TWILIO",
                "AUTH_TOKEN",
                authToken
        );

        redirectAttributes.addFlashAttribute(
                "success",
                "WhatsApp credentials saved successfully."
        );

        return "redirect:/profile/credentials";

    }

    @PostMapping("/whatsapp/delete")
    public String deleteWhatsapp(RedirectAttributes redirectAttributes) {

        userCredentialService.deleteTwilioCredentials();

        redirectAttributes.addFlashAttribute(
                "success",
                "WhatsApp credentials removed successfully."
        );

        return "redirect:/profile/credentials";

    }



}
