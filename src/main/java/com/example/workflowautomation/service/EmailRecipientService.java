package com.example.workflowautomation.service;


import com.example.workflowautomation.entity.EmailRecipient;
import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.repository.EmailRecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class EmailRecipientService {

    private final EmailRecipientRepository emailRecipientRepository;
    private final WorkflowService workflowService;


    public List<EmailRecipient> getCurrentUserRecipients() {

        User currentUser = workflowService.getCurrentUser();

        return emailRecipientRepository.findByUser(currentUser);
    }


    public List<EmailRecipient> getCurrentUserActiveRecipients() {

        User currentUser = workflowService.getCurrentUser();

        return emailRecipientRepository.findByUserAndActiveTrue(currentUser);
    }


    public EmailRecipient addRecipient(
            String name,
            String email) {

        User currentUser = workflowService.getCurrentUser();

        EmailRecipient recipient = EmailRecipient.builder()
                .name(name)
                .email(email)
                .active(true)
                .user(currentUser)
                .build();

        return emailRecipientRepository.save(recipient);
    }


    public void deleteRecipient(Long recipientId) {

        User currentUser = workflowService.getCurrentUser();

        EmailRecipient recipient =
                emailRecipientRepository.findById(recipientId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recipient not found"
                                )
                        );

        if (!recipient.getUser().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not allowed to delete this recipient"
            );
        }

        emailRecipientRepository.delete(recipient);
    }


    public void setActive(
            Long recipientId,
            boolean active) {

        User currentUser = workflowService.getCurrentUser();

        EmailRecipient recipient =
                emailRecipientRepository.findById(recipientId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recipient not found"
                                )
                        );

        if (!recipient.getUser().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not allowed to modify this recipient"
            );
        }

        recipient.setActive(active);

        emailRecipientRepository.save(recipient);
    }
}