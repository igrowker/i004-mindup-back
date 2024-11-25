package com.mindup.core.validations;

import com.mindup.core.exceptions.ProfileImageValidationException;
import org.apache.commons.validator.routines.UrlValidator;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
            throw new ProfileImageValidationException(
                    "Profile image URL must point to a valid image format: " + profileImageUrl);
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(profileImageUrl).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            String contentType = connection.getContentType();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new ProfileImageValidationException(
                        "Profile image URL is not accessible. Response code: " + responseCode);
            }

            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ProfileImageValidationException(
                        "The URL does not point to an image. Content type: " + contentType);
            }

        } catch (IOException e) {
            throw new ProfileImageValidationException("Unable to connect to the profile image URL: " + profileImageUrl);
        }
    }
}
