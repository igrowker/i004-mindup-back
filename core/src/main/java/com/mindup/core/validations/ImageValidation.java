package com.mindup.core.validations;

import com.mindup.core.exceptions.ImageValidationException;
import org.apache.commons.validator.routines.UrlValidator;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageValidation {

    private static final String[] SUPPORTED_SCHEMES = {"http", "https"};
    private static final String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};

    public static void validateimage(String image) {
        if (image == null || image.isBlank()) {
            throw new ImageValidationException("Profile image URL cannot be null or blank.");
        }

        UrlValidator urlValidator = new UrlValidator(SUPPORTED_SCHEMES);
        if (!urlValidator.isValid(image)) {
            throw new ImageValidationException("Invalid profile image URL: " + image);
        }

        boolean hasValidExtension = false;
        for (String extension : IMAGE_EXTENSIONS) {
            if (image.toLowerCase().endsWith(extension)) {
                hasValidExtension = true;
                break;
            }
        }

        if (!hasValidExtension) {
            throw new ImageValidationException("Profile image URL must point to a valid image format: " + image);
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(image).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            String contentType = connection.getContentType();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new ImageValidationException("Profile image URL is not accessible. Response code: " + responseCode);
            }

            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ImageValidationException("The URL does not point to an image. Content type: " + contentType);
            }

        } catch (IOException e) {
            throw new ImageValidationException("Unable to connect to the profile image URL: " + image);
        }
    }
}
