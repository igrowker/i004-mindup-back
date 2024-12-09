package com.mindup.core.validations;

import com.mindup.core.dtos.User.*;
import com.mindup.core.entities.User;
import com.mindup.core.enums.Role;
import com.mindup.core.repositories.UserRepository;
import com.mindup.core.enums.Gender;
import com.mindup.core.utils.EmailUtils;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.regex.Pattern;
import java.time.LocalDate;

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
        if (userProfileDTO.getBirth() != null) {
            if (!isValidBirth(userProfileDTO.getBirth())) {
                throw new IllegalArgumentException("Invalid birth date. It must be in the past.");
            }
        }
        if (userProfileDTO.getTuition() != null && !isValidTuition(userProfileDTO.getTuition())) {
            throw new IllegalArgumentException("Invalid tuition format. It should be alphanumeric.");
        }
        if (userProfileDTO.getSpecialty() != null && !isValidSpecialty(userProfileDTO.getSpecialty())) {
            throw new IllegalArgumentException("Specialty must be a valid string with letters and spaces.");
        }
        if (userProfileDTO.getGender() != null && !isValidGender(userProfileDTO.getGender())) {
            throw new IllegalArgumentException("Invalid gender. It must be 'Male' or 'Female'.");
        }
    }

    public static boolean isValidName(String name) {
        return name != null && name.length() >= 2;
    }

    public static boolean isValidBirth(LocalDate birth) {
        return birth.isBefore(LocalDate.now());
    }

    public static boolean isValidTuition(String tuition) {
        return tuition.matches("^[A-Za-z0-9]+$");
    }

    public static boolean isValidSpecialty(String specialty) {
        return specialty.matches("^[A-Za-z\\s]+$");
    }

    public static boolean isValidGender(Gender gender) {
        return gender == Gender.MALE || gender == Gender.FEMALE || gender == Gender.OTHER;
    }

    public static boolean isValidInformation(String information) {
        return information.length() <= 500;
    }

    public static boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
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