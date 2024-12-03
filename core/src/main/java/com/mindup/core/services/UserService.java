package com.mindup.core.services;

import com.mindup.core.dtos.User.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Optional;

public interface UserService {

    UserDTO registerUser(UserRegisterDTO userRegisterDTO);

    Optional<UserDTO> findUserByEmail(String email);

    Optional<UserProfileDTO> findUserById(String userId);

    void changePassword(String userId, String currentPassword, String newPassword);

    ResponseLoginDto authenticateUser(String email, String password);

    void updateUser(UserDTO userDTO);

    void deleteUserAccount(String email);

    void updateProfileImage(String userId, ProfileImageDTO profileImageDTO);

    UserDTO toggleAvailability(String id) throws IOException;

    void deleteProfileImage(String userId);

    void updateUserProfile(String userId, String name, UserProfileDTO userProfileDTO);

    UserProfileDTO getUserProfile(String userId);

    Boolean findProfessionalByUserIdAndRole(String id);

    Boolean findPatientByUserIdAndRole(String id);

    ResponseEntity<String> requestPasswordReset(String email);

    ResponseEntity<String> resetPassword(String token, String newPassword);

    void updateProfileVideo(String userId, ProfileVideoDTO profileVideoDTO);

    void deleteProfileVideo(String userId);
}
