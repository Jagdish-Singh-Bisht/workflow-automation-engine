package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;



public interface StudentRepository extends JpaRepository<Student, Long> {

}
