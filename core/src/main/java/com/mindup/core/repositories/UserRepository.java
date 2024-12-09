package com.mindup.core.repositories;

import com.mindup.core.entities.User;
import com.mindup.core.enums.*;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.role = :role")
    Optional<User> findUserByUserIdAndRole(String userId, Role role);

    @Query("SELECT u FROM User u WHERE u.role = 'PSYCHOLOGIST' AND u.gender = :gender")
    List<User> findPsychologistsByGender(@Param("gender") Gender gender);
    
    List<User> findByRole(Role role);
}
