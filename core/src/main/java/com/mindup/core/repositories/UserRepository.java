package com.mindup.core.repositories;

import com.mindup.core.entities.User;
import com.mindup.core.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.role = :role")
    Optional<User> findUserByUserIdAndRole(String userId, Role role);

    @Query("SELECT u FROM User u WHERE u.role = 'PSYCHOLOGIST' "
            + "AND (:isBelow35 IS NULL OR (:isBelow35 = true AND u.age < 35) OR (:isBelow35 = false AND u.age >= 35)) "
            + "AND (:gender IS NULL OR u.gender = :gender)")
    List<User> findPsychologistsByPreferences(Boolean isBelow35, Gender gender);
}
