package com.example.workflowautomation.service;


import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.entity.UserCredential;
import com.example.workflowautomation.repository.UserCredentialRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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
                .findByUserAndProviderAndCredentialType(
                        currentUser,
                        provider,
                        credentialType
                )
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

    public boolean hasCredential(String provider) {

        User currentUser = workflowService.getCurrentUser();

        return userCredentialRepository
                .findByUserAndProvider(currentUser, provider)
                .isPresent();

    }

    @Transactional
    public void deleteCredential(String provider) {

        User currentUser = workflowService.getCurrentUser();

        userCredentialRepository.deleteByUserAndProvider(
                currentUser,
                provider
        );

    }

    public boolean hasEmailCredential() {

        User currentUser = workflowService.getCurrentUser();

        return userCredentialRepository
                .findByUserAndProviderAndCredentialType(
                        currentUser,
                        "EMAIL",
                        "USERNAME"
                )
                .isPresent()
                &&
                userCredentialRepository
                        .findByUserAndProviderAndCredentialType(
                                currentUser,
                                "EMAIL",
                                "APP_PASSWORD"
                        )
                        .isPresent();

    }

    @Transactional
    public void deleteEmailCredentials() {

        User currentUser = workflowService.getCurrentUser();

        List<UserCredential> credentials =
                userCredentialRepository.findByUser(currentUser);

        credentials.stream()
                .filter(credential ->
                        "EMAIL".equals(credential.getProvider()))
                .forEach(userCredentialRepository::delete);

    }

    public boolean hasTwilioCredential() {

        User currentUser = workflowService.getCurrentUser();

        return userCredentialRepository
                .findByUserAndProviderAndCredentialType(
                        currentUser,
                        "TWILIO",
                        "ACCOUNT_SID"
                )
                .isPresent()
                &&
                userCredentialRepository
                        .findByUserAndProviderAndCredentialType(
                                currentUser,
                                "TWILIO",
                                "AUTH_TOKEN"
                        )
                        .isPresent();

    }

    @Transactional
    public void deleteTwilioCredentials() {

        User currentUser = workflowService.getCurrentUser();

        List<UserCredential> credentials =
                userCredentialRepository.findByUser(currentUser);

        credentials.stream()
                .filter(credential ->
                        "TWILIO".equals(credential.getProvider()))
                .forEach(userCredentialRepository::delete);

    }

    public String getDecryptedCredential(User user,
                                         String provider,
                                         String credentialType ) {

        UserCredential credential =
                userCredentialRepository
                        .findByUserAndProviderAndCredentialType(
                                user,
                                provider,
                                credentialType
                        )
                        .orElseThrow(() -> new RuntimeException(
                                "Credential not configured: "
                                        + provider
                                        + " / "
                                        + credentialType
                                )
                        );

        return credentialEncryptionService.decrypt(
                credential.getEncryptedValue()
        );

    }






}
