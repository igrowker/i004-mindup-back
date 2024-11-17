package com.mindup.core.controllers;

import com.mindup.core.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mindup.core.dtos.*;
import com.mindup.core.utils.EmailUtils;
import com.mindup.core.validations.*;

@RestController
@RequestMapping("/api/core")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        UserValidation.validateUserRegister(userRegisterDTO);
        UserDTO userDTO = userService.registerUser(userRegisterDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDto> loginUser(@RequestBody @Valid UserLoginDTO loginDTO) {
        UserValidation.validateLoginData(loginDTO);
        var userLogged = userService.authenticateUser(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok(userLogged);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestParam String email) {
        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/user/password")
    public ResponseEntity<String> changePassword(@RequestParam String email, @RequestBody String newPassword) {
        try {
            if (!EmailUtils.isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email format");
            }
            PasswordValidation.validatePassword(newPassword);
            userService.changePassword(email, newPassword);
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @PutMapping("/user/preferences")
    public ResponseEntity<UserDTO> updateUserPreferences(@RequestParam String email, @RequestBody String preferences) {
        return userService.findUserByEmail(email)
                .map(user -> {
                    user.setPreferences(preferences);
                    userService.updateUser(user); 
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
