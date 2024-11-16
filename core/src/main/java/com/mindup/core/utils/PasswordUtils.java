package com.mindup.core.utils;

import java.util.UUID;

public class PasswordUtils {

    public static String generateRandomPassword() {
        return UUID.randomUUID().toString();
    }

    public static boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }
}
