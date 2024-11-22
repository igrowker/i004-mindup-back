package com.mindup.core.validations;

import com.mindup.core.exceptions.ProfileImageValidationException;
import org.apache.commons.validator.routines.UrlValidator;

public class ProfileImageValidation {

    private static final String[] SUPPORTED_SCHEMES = {"http", "https"};
    private static final String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};

    public static void validateProfileImageUrl(String profileImageUrl) {
        if (profileImageUrl == null || profileImageUrl.isBlank()) {
            throw new ProfileImageValidationException("Profile image URL cannot be null or blank.");
        }

        UrlValidator urlValidator = new UrlValidator(SUPPORTED_SCHEMES);
        if (!urlValidator.isValid(profileImageUrl)) {
            throw new ProfileImageValidationException("Invalid profile image URL: " + profileImageUrl);
        }

        boolean hasValidExtension = false;
        for (String extension : IMAGE_EXTENSIONS) {
            if (profileImageUrl.toLowerCase().endsWith(extension)) {
                hasValidExtension = true;
                break;
            }
        }

        if (!hasValidExtension) {
            throw new ProfileImageValidationException("Profile image URL must point to a valid image format: " + profileImageUrl);
        }
    }
}
