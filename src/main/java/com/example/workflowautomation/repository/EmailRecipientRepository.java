package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.User;
import com.example.workflowautomation.entity.EmailRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface EmailRecipientRepository extends JpaRepository<EmailRecipient, Long> {

    List<EmailRecipient> findByUser(User user);

    List<EmailRecipient> findByUserAndActiveTrue(User user);

}
