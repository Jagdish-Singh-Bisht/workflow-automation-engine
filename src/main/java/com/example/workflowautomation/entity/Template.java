package com.example.workflowautomation.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String subjectTemplate;

    @Column(columnDefinition = "TEXT")
    private String bodyTemplate;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public String getSubjectTemplate() {
        return subjectTemplate;
    }
}
