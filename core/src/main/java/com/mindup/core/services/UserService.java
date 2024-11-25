package com.mindup.core.services;

import com.mindup.core.dtos.User.*;

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

    void updateUserProfile(String userId, UserProfileDTO userProfileDTO);

    UserProfileDTO getUserProfile(String userId);

    Boolean findProfessionalById(String id);
}