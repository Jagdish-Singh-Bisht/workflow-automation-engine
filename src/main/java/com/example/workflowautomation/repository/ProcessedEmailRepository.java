package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.ProcessedEmail;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProcessedEmailRepository extends JpaRepository<ProcessedEmail, Long> {

    boolean existsByMessageId(String messageId);
}
