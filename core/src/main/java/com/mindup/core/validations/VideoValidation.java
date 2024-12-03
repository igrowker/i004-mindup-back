package com.mindup.core.validations;

import com.mindup.core.exceptions.VideoValidationException;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class VideoValidation {

    private static final String[] SUPPORTED_SCHEMES = {"http", "https"};

    public static void validateVideo(String videoUrl) {
        if (videoUrl == null || videoUrl.isBlank()) {
            throw new VideoValidationException("Video URL cannot be null or blank.");
        }

        UrlValidator urlValidator = new UrlValidator(SUPPORTED_SCHEMES);
        if (!urlValidator.isValid(videoUrl)) {
            throw new VideoValidationException("Invalid video URL: " + videoUrl);
        }

        if (videoUrl.contains("youtube.com") || videoUrl.contains("youtu.be")) {
            System.out.println("YouTube video URL is valid.");
        } 
        else if (videoUrl.contains("drive.google.com")) {
            if (!videoUrl.contains("/file/d/") || !videoUrl.contains("/view")) {
                throw new VideoValidationException("Invalid Google Drive video URL format.");
            }
            System.out.println("Google Drive video URL is valid.");
        } 
        else {
            System.out.println("This is a valid video URL, but it is not from YouTube or Google Drive.");
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(videoUrl).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new VideoValidationException("Video URL is not accessible. Response code: " + responseCode);
            }

        } catch (IOException e) {
            throw new VideoValidationException("Unable to connect to the video URL: " + videoUrl);
        }
    }
}
