package com.example.workflowautomation.service;


import com.example.workflowautomation.entity.User;
//import com.example.workflowautomation.service.WorkflowService;
import com.example.workflowautomation.entity.UserCredential;
import com.example.workflowautomation.repository.UserCredentialRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;




@Service
@RequiredArgsConstructor
public class UserCredentialService {

    private final WorkflowService workflowService;
    private final UserCredentialRepository userCredentialRepository;
    private final CredentialEncryptionService credentialEncryptionService;


    public List<UserCredential> getCurrentUserCredentials() {

        User currentUser = workflowService.getCurrentUser();

        return userCredentialRepository
                .findByUser(currentUser);

    }

    public UserCredential getCurrentUserCredential(String provider) {

        User currentUser = workflowService.getCurrentUser();

        return userCredentialRepository
                .findByUserAndProvider(currentUser, provider)
                .orElseThrow(() -> new RuntimeException("Credentials not found"));

    }

    public UserCredential saveCredential(
            String provider,
            String credentialType,
            String credentialValue) {

        User currentUser = workflowService.getCurrentUser();

        String encryptedValue =
                credentialEncryptionService.encrypt(credentialValue);

        UserCredential credential = userCredentialRepository
                .findByUserAndProvider(currentUser, provider)
                .orElse(
                        UserCredential.builder()
                                .user(currentUser)
                                .provider(provider)
                                .credentialType(credentialType)
                                .build()
                );

        credential.setCredentialType(credentialType);
        credential.setEncryptedValue(encryptedValue);
        credential.setUpdatedAt(LocalDateTime.now());

        if(credential.getCreatedAt() == null) {
            credential.setCreatedAt(LocalDateTime.now());
        }

        return userCredentialRepository.save(credential);

    }


}
