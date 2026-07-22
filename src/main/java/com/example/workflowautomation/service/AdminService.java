package com.example.workflowautomation.service;


import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.service.WorkflowService;
import com.example.workflowautomation.repository.WorkflowRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;



@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final WorkflowService workflowService;
    private final WorkflowRepository workflowRepository;


    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }

    /*
    Delete Request
    Does user exist?
    Is it the current user?
    Is this the last ADMIN?
     */


    private void validateWorkflowOwnership(User user) {

        if(workflowRepository.existsByUser(user)) {
            throw new RuntimeException("Cannot delete user because they own workflow. \n Transfer or delete their workflows first.");
        }
    }

    public void deleteUser(Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User currentUser = workflowService.getCurrentUser();

        // ValidateNotCurrentUser
        if(user.getId().equals(currentUser.getId())) {
            throw new RuntimeException("Cannot delete your own account");
        }

        // ValidateLastAdmin
        if("ADMIN".equals(user.getRole())) {
            long adminCount = userRepository.countByRole("ADMIN");

            if(adminCount == 1) {
                throw new RuntimeException("Cannot delete the last admin");
            }
        }

        validateWorkflowOwnership(user);

        userRepository.delete(user);

    }
}
