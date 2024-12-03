package com.mindup.core.exceptions;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "User Conflict");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "User Not Found");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", "An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ImageValidationException.class)
    public ResponseEntity<Map<String, String>> handleProfileImageValidationError(ImageValidationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Profile Image");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(PhoneNumberFormatException.class)
    public ResponseEntity<Map<String, String>> handlePhoneNumberFormatException(PhoneNumberFormatException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Phone Number Format");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AppointmentConflictException.class)
    public ResponseEntity<Map<String, String>> handleAppointmentConflictException(AppointmentConflictException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid appointment time");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePsychologistNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid psycholohist id");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RoleMismatchException.class)
    public ResponseEntity<Map<String, String>> handleRoleMismatchException(RoleMismatchException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid role request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPassword(InvalidPasswordException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Password");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Input");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EmptyAppointmentsByStateException.class)
    public ResponseEntity<Map<String, String>> handleEmptyAppointmentsByStateException(EmptyAppointmentsByStateException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "State without appointments");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EmptyAppointmentException.class)
    public ResponseEntity<Map<String, String>> handleEmptyAppointmentException(EmptyAppointmentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Empty appointments");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidNameException.class)
    public ResponseEntity<Map<String, String>> handleInvalidNameException(InvalidNameException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Name");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidTuitionException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTuitionException(InvalidTuitionException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Tuition");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidSpecialtyException.class)
    public ResponseEntity<Map<String, String>> handleInvalidSpecialtyException(InvalidSpecialtyException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Specialty");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BirthUpdateException.class)
    public ResponseEntity<Map<String, String>> handleBirthUpdateError(BirthUpdateException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Birth Date Update Error");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidGenderException.class)
    public ResponseEntity<Map<String, String>> handleInvalidGenderException(InvalidGenderException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Gender");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InformationUpdateException.class)
    public ResponseEntity<Map<String, String>> handleInformationUpdateError(InformationUpdateException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "About Me Update Error");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidLocationException.class)
    public ResponseEntity<Map<String, String>> handleInvalidLocationException(InvalidLocationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Location");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(VideoValidationException.class)
    public ResponseEntity<Map<String, String>> handleVideoValidationError(VideoValidationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Video URL");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidPreferencesException.class)
    public ResponseEntity<String> handleInvalidPreferences(InvalidPreferencesException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
