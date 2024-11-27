package com.mindup.core.services;

import com.mindup.core.dtos.User.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {

    UserDTO registerUser(UserRegisterDTO userRegisterDTO);

    Optional<UserDTO> findUserByEmail(String email);

    void changePassword(String email, String newPassword);

    ResponseLoginDto authenticateUser(String email, String password);

    void updateUser(UserDTO userDTO);

    void deleteUserAccount(String email);
    
    void updateProfileImage(String userId, ProfileImageDTO profileImageDTO);

    UserDTO toggleAvailability(String id);

    void deleteProfileImage(String userId);

    public void updateUserProfile(String userId, UserProfileDTO userProfileDTO);

    public UserProfileDTO getUserProfile(String userId);

    ResponseEntity<String> requestPasswordReset(String email);

    ResponseEntity<String> resetPassword(String token, String newPassword);
}