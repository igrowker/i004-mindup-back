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

    @ExceptionHandler(ProfileImageValidationException.class)
    public ResponseEntity<Map<String, String>> handleProfileImageValidationError(ProfileImageValidationException ex) {
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
    public ResponseEntity<Map<String,String>> handleAppointmentConflictException(AppointmentConflictException ex) {
        Map <String ,String> response = new HashMap<>();
        response.put("error", "Invalid appointment time");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String,String>> handlePatientNotFoundException(PatientNotFoundException ex) {
        Map <String ,String> response = new HashMap<>();
        response.put("error", "Invalid patient id");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(PsychologistNotFoundException.class)
    public ResponseEntity<Map<String,String>> handlePsychologistNotFoundException(PsychologistNotFoundException ex) {
        Map <String ,String> response = new HashMap<>();
        response.put("error", "Invalid psycholohist id");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RoleMismatchException.class)
    public ResponseEntity<Map<String,String>> handleRoleMismatchException(RoleMismatchException ex) {
        Map <String ,String> response = new HashMap<>();
        response.put("error", "Invalid role request");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
