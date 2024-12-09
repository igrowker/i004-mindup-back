package com.mindup.core.controllers;

import com.mindup.core.dtos.PasswordReset.*;
import com.mindup.core.dtos.User.*;
import com.mindup.core.entities.*;
import com.mindup.core.enums.Role;
import com.mindup.core.exceptions.*;
import com.mindup.core.repositories.UserRepository;
import com.mindup.core.security.JwtService;
import com.mindup.core.services.*;
import com.mindup.core.utils.UserValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/core")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;
    private final PatientService patientService;
    private final JwtService jwtService;
    private final UserValidationUtil userValidationUtil;

    private void validateUserId(HttpServletRequest request, String userId, String expectedRole) {
        String token = request.getHeader("Authorization").substring(7);
        String currentUserId = jwtService.extractUserId(token);
        String currentUserRole = jwtService.extractRole(token);

        if (!currentUserId.equals(userId)) {
            throw new SecurityException("Unauthorized access to another user's data.");
        }
        if (expectedRole != null && !currentUserRole.equals(expectedRole)) {
            throw new SecurityException("Unauthorized role access.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        UserDTO userDTO = userService.registerUser(userRegisterDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDto> loginUser(@RequestBody @Valid UserLoginDTO loginDTO) {
        ResponseLoginDto responseLoginDto = userService.authenticateUser(loginDTO.getEmail(), loginDTO.getPassword());

        if (responseLoginDto != null) {
            Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                EmailVerification emailVerification = emailVerificationService.findByUser(user);

                if (emailVerification != null && emailVerification.isVerified()) {
                    return ResponseEntity.ok(responseLoginDto);
                } else {
                    return ResponseEntity.status(403).body(new ResponseLoginDto(null, null, null, null, null, "Account not verified. Please verify your email first."));
                }
            } else {
                return ResponseEntity.status(404).body(new ResponseLoginDto(null, null, null, null, null, "User not found."));
            }
        } else {
            return ResponseEntity.status(401).body(new ResponseLoginDto(null, null, null, null, null, "Invalid credentials."));
        }
    }

    @GetMapping("/user/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestParam String email) {
        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable String userId,
            @RequestBody ChangePasswordDTO changePasswordDTO,
            HttpServletRequest request) {
        try {
            userValidationUtil.validateUserId(request, userId, null);
            userService.changePassword(userId, changePasswordDTO.getCurrentPassword(), changePasswordDTO.getNewPassword());
            return ResponseEntity.ok("Password updated successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid current password.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/user/preferences")
    public ResponseEntity<UserDTO> updateUserPreferences(@RequestParam String email, @RequestBody @Valid String preferences) {
        return userService.findUserByEmail(email)
                .map(user -> {
                    user.setPreferences(preferences);
                    userService.updateUser(user);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/user/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        return ResponseEntity.ok("User logged out successfully.");
    }

    @DeleteMapping("/user/delete-account")
    public ResponseEntity<String> deleteUserAccount(
            @RequestParam String email,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String currentUserEmail = jwtService.extractEmail(token);
        if (!currentUserEmail.equals(email)) {
            throw new SecurityException("Unauthorized access to another user's account.");
        }
        userService.deleteUserAccount(email);
        return ResponseEntity.ok("User account deleted successfully.");
    }

    @PostMapping("/user/{userId}/profile-image/update")
    public ResponseEntity<String> updateProfileImage(
            @PathVariable String userId,
            @RequestBody @Valid ProfileImageDTO profileImageDTO,
            HttpServletRequest request) {
        userValidationUtil.validateUserId(request, userId, null);
        userService.updateProfileImage(userId, profileImageDTO);
        return ResponseEntity.ok("Profile image updated successfully.");
    }

    @DeleteMapping("/user/{userId}/profile-image/delete")
    public ResponseEntity<String> deleteProfileImage(
            @PathVariable String userId,
            HttpServletRequest request) {
        userValidationUtil.validateUserId(request, userId, null);
        userService.deleteProfileImage(userId);
        return ResponseEntity.ok("Profile image deleted successfully.");
    }

    @PutMapping("/user/availability/{professionalId}")
    public ResponseEntity<?> toggleAvailability(@PathVariable String professionalId) {
        try {
            UserDTO user = userService.toggleAvailability(professionalId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse() {
                        @Override
                        public HttpStatusCode getStatusCode() {
                            return null;
                        }

                        @Override
                        public ProblemDetail getBody() {
                            return null;
                        }
                    });
        }
    }

    @GetMapping("/user/profile/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfileById(
            @PathVariable String userId) {
        UserProfileDTO userProfile = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/user/{userId}/profile")
    public ResponseEntity<String> updateUserProfile(
            @PathVariable String userId,
            @Valid @RequestBody UserProfileDTO userProfileDTO,
            HttpServletRequest request) {
        userValidationUtil.validateUserId(request, userId, null);
        userService.updateUserProfile(userId, userProfileDTO.getName(), userProfileDTO);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    @GetMapping("/user/professional/{id}")
    public ResponseEntity<Boolean> findProfessionalByUserIdAndRole(@PathVariable String id) {
        userService.findProfessionalByUserIdAndRole(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/patient/{id}")
    public ResponseEntity<Boolean> findPatientByUserIdAndRole(@PathVariable String id) {
        userService.findPatientByUserIdAndRole(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requestPwReset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody @Valid PasswordResetRequestDTO requestDTO) {
        ResponseEntity<String> response = userService.requestPasswordReset(requestDTO.getEmail());
        return response;
    }

    @PostMapping("/resetPW")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetDTO resetDTO) {
        return userService.resetPassword(resetDTO.getToken(), resetDTO.getNewPassword());
    }

    @PostMapping("/user/{userId}/profile-video/update")
    public ResponseEntity<String> updateProfileVideo(
            @PathVariable String userId,
            @RequestBody @Valid ProfileVideoDTO profileVideoDTO,
            HttpServletRequest request) {
        userValidationUtil.validateUserId(request, userId, "PSYCHOLOGIST");
        userService.updateProfileVideo(userId, profileVideoDTO);
        return ResponseEntity.ok("Profile video updated successfully.");
    }

    @DeleteMapping("/user/{userId}/profile-video/delete")
    public ResponseEntity<String> deleteProfileVideo(
            @PathVariable String userId,
            HttpServletRequest request) {
        userValidationUtil.validateUserId(request, userId, "PSYCHOLOGIST");
        userService.deleteProfileVideo(userId);
        return ResponseEntity.ok("Profile video deleted successfully.");
    }

    @PostMapping("/search-preference-psychologists")
    public ResponseEntity<?> searchPsychologists(
            @RequestBody PatientPreferencesDTO preferencesDTO,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String currentUserRole = jwtService.extractRole(token);
        if (!"PATIENT".equals(currentUserRole)) {
            throw new SecurityException("Access is denied for this role.");
        }
        patientService.validatePreferences(preferencesDTO);
        List<User> psychologists = patientService.searchPsychologists(preferencesDTO);
        if (psychologists.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No psychologists were found with the requested preferences");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(psychologists);
    }

    @PostMapping("/view-psychologists")
    public ResponseEntity<?> viewPsychologists(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String currentUserRole = jwtService.extractRole(token);
        if (!"PATIENT".equals(currentUserRole)) {
            throw new SecurityException("Access is denied for this role.");
        }
        List<User> psychologists = userRepository.findByRole(Role.PSYCHOLOGIST);
        if (psychologists.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No psychologists found in the database.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(psychologists);
    }

    @PostMapping("/view-patients")
    public ResponseEntity<?> viewPatients(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String currentUserRole = jwtService.extractRole(token);
        if (!"PSYCHOLOGIST".equals(currentUserRole)) {
            throw new SecurityException("Access is denied for this role.");
        }
        List<User> patients = userRepository.findByRole(Role.PATIENT);
        if (patients.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No patients found in the database.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(patients);
    }
}
