package com.mindup.core.validations;

import com.mindup.core.dtos.User.*;
import com.mindup.core.entities.User;
import com.mindup.core.enums.Role;
import com.mindup.core.repositories.UserRepository;
import com.mindup.core.utils.EmailUtils;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserValidation {

    private final UserRepository userRepository;
    private User currentUser; // Almacena el usuario de la sesión actual

    public static void validateLoginData(UserLoginDTO loginDTO) {
        if (loginDTO.getEmail() == null || loginDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!EmailUtils.isValidEmail(loginDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    public static void validateUserRegister(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO.getName() == null || userRegisterDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (userRegisterDTO.getName().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters long");
        }

        if (userRegisterDTO.getEmail() == null || userRegisterDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!EmailUtils.isValidEmail(userRegisterDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (userRegisterDTO.getRole() == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }

    private static final String PHONE_PATTERN = "^\\+?[1-9]\\d{1,14}$";

    public static void validateUserProfile(UserProfileDTO userProfileDTO) {
        if (userProfileDTO.getPhone() != null && !userProfileDTO.getPhone().isEmpty()) {
            if (!Pattern.matches(PHONE_PATTERN, userProfileDTO.getPhone())) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }
    }

    // Establece el usuario actual de la sesión
    public boolean setCurrentUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        System.out.println(user);
        if (user.isPresent()) {
            this.currentUser = user.get();
            return true;
        }
        return false;
    }

    // Obtiene el usuario autenticado actual
    public User getCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("No hay usuario autenticado");
        }
        return currentUser;
    }

    public boolean isPatient() {
        return getCurrentUser().getRole() == Role.PATIENT;
    }

    public boolean isPsychologist() {
        return getCurrentUser().getRole() == Role.PSYCHOLOGIST;
    }

    public boolean hasRole(Role requiredRole) {
        return getCurrentUser().getRole() == requiredRole;
    }
}