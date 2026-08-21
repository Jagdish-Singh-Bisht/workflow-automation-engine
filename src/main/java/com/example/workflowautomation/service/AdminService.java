package com.example.workflowautomation.service;


import com.example.workflowautomation.entity.Workflow;
import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.repository.UserRepository;
import com.example.workflowautomation.service.WorkflowService;
import com.example.workflowautomation.repository.WorkflowRepository;

import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
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

        List<Workflow> workflows = workflowRepository.findByUser(user);

        for(Workflow workflow : workflows) {
            workflowService.deleteWorkflowAsAdmin(workflow.getId());
        }

        userRepository.delete(user);

    }
}
