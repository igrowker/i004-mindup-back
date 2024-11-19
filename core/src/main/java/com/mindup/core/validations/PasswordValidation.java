package com.mindup.core.validations;

import com.mindup.core.utils.PasswordUtils;

public class PasswordValidation {

    public static void validatePassword(String password) {
        if (!PasswordUtils.isValidPassword(password)) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters long, contain at least one uppercase letter, "
                    + "one lowercase letter, one number, and one special character (@$!%*?&)."
            );
        }
    }
}
