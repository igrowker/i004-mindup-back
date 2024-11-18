package com.mindup.core.services;

import com.mindup.core.dtos.*;
import java.util.Optional;

public interface UserService {

    UserDTO registerUser(UserRegisterDTO userRegisterDTO);

    Optional<UserDTO> findUserByEmail(String email);

    void changePassword(String email, String newPassword);

    ResponseLoginDto authenticateUser(String email, String password);

    void updateUser(UserDTO userDTO);
}
