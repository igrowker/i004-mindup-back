package com.mindup.core.repositories;

import com.mindup.core.entities.EmailVerification;
import com.mindup.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {
    Optional<EmailVerification> findByVerificationToken(String token);
    EmailVerification findByUser (User user);

}
