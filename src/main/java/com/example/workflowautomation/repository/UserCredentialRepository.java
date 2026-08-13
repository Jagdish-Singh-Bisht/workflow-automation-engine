package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.UserCredential;
import com.example.workflowautomation.entity.User;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;



public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    List<UserCredential> findByUser(User user);

    Optional<UserCredential> findByUserAndProvider(User user, String provider);

//    @Transactional
    void deleteByUserAndProvider(User user, String provider);

    Optional<UserCredential> findByUserAndProviderAndCredentialType(User user, String provider, String credentialType);

//    void deleteByUserAndProviderAndCredentialType(User user, String provider, String credentialType);


}
