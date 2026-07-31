package com.example.workflowautomation.service;


import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.dto.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final WorkflowService workflowService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(ChangePasswordRequest request) {

        User currentUser = workflowService.getCurrentUser();

        // current password should be matched to change password
        if(!passwordEncoder.matches(
                request.getCurrentPassword(), currentUser.getPassword())) {

            throw new RuntimeException("Current password is incorrect.");
        }

        // New password should be matched while Confirming password
        if(!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new RuntimeException("Password does not match.");
        }

        // Old pass and New pass must be different
        if(passwordEncoder.matches(
                request.getNewPassword(), currentUser.getPassword())) {

            throw new RuntimeException("New password must be different.");
        }

        if(request.getNewPassword().length() < 8) {
            throw new RuntimeException("Password must contain at least 8 characters.");
        }

        String encodedPassword =
                passwordEncoder.encode(request.getNewPassword());

        currentUser.setPassword(encodedPassword);

        userRepository.save(currentUser);


    }
}
