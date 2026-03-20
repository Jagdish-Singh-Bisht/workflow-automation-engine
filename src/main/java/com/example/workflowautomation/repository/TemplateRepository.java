package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findByName(String name);

}
