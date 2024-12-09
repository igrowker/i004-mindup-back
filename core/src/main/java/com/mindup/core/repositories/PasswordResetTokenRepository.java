package com.mindup.core.repositories;

import com.mindup.core.entities.PasswordResetToken;
import com.mindup.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
    List<PasswordResetToken> findAllByExpiryDateBefore(LocalDateTime expiryDate);

}
