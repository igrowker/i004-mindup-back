package com.mindup.core.validations;

import com.mindup.core.dtos.*;
import com.mindup.core.dtos.User.UserLoginDTO;
import com.mindup.core.dtos.User.UserRegisterDTO;
import com.mindup.core.utils.EmailUtils;

public class UserValidation {

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
}
