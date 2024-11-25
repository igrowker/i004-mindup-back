package com.mindup.core.repositories;

import com.mindup.core.entities.User;
import com.mindup.core.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findProfessionalById(String userId, Role role);
}
