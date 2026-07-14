package com.example.workflowautomation.service;


import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.dto.RegisterRequest;
import com.example.workflowautomation.exception.UsernameAlreadyExistsException;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;




@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public User registerUser(RegisterRequest request) {

        System.out.println("Register service called");

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();


        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole("USER");

        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);

    }
}
