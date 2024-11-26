package com.mindup.core.services;

import com.mindup.core.dtos.User.*;

import java.util.Optional;

public interface UserService {

    UserDTO registerUser(UserRegisterDTO userRegisterDTO);

    Optional<UserDTO> findUserByEmail(String email);

    void changePassword(String userId, String currentPassword, String newPassword);

    ResponseLoginDto authenticateUser(String email, String password);

    void updateUser(UserDTO userDTO);

    void deleteUserAccount(String email);
    
    void updateProfileImage(String userId, ProfileImageDTO profileImageDTO);

    UserDTO toggleAvailability(String id);

    void deleteProfileImage(String userId);

    public void updateUserProfile(String userId, UserProfileDTO userProfileDTO);

    public UserProfileDTO getUserProfile(String userId);

    void requestPasswordReset(String email);

    void resetPassword(String token, String newPassword);
}