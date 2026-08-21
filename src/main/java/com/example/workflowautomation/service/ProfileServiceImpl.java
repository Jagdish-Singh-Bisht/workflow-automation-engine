package com.example.workflowautomation.service;



import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.dto.ChangePasswordRequest;
import com.example.workflowautomation.dto.ChangeUsernameRequest;
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

        if(!passwordEncoder.matches(
                request.getCurrentPassword(), currentUser.getPassword())) {

            throw new RuntimeException("Current password is incorrect.");
        }

        if(!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new RuntimeException("Password does not match.");
        }

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

    @Override
    public void changeUsername(ChangeUsernameRequest request) {

        User currentUser = workflowService.getCurrentUser();

        if(!passwordEncoder.matches(
                request.getCurrentPassword(),
                currentUser.getPassword())) {

            throw new RuntimeException("Current password is incorrect");
        }

        if(request.getNewUsername() == null ||
        request.getNewUsername().trim().isEmpty()) {

            throw new RuntimeException("Username cannot be empty");
        }

        if(currentUser.getUsername()
                .equals(request.getNewUsername()) ) {

            throw new RuntimeException("New Username must be different");
        }

        if(userRepository.findByUsername(
                request.getNewUsername())
                .isPresent()) {

            throw new RuntimeException("Username already exists");
        }

        currentUser.setUsername(request.getNewUsername());

        userRepository.save(currentUser);

    }
}
