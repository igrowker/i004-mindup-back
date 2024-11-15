package com.mindup.core.controllers;

import com.mindup.core.entities.User;
import com.mindup.core.enums.Role;
import com.mindup.core.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody Map<String, String> userDto) {
        User user = userService.registerUser(
                userDto.get("name"),
                userDto.get("email"),
                userDto.get("password"),
                Role.valueOf(userDto.get("role").toUpperCase())
        );
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginDto) {
        String email = loginDto.get("email");
        String password = loginDto.get("password");

        if (userService.authenticateUser(email, password)) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/user/profile")
    public ResponseEntity<User> getUserProfile(@RequestParam String email) {
        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/user/password")
    public ResponseEntity<String> changePassword(@RequestParam String email, @RequestBody Map<String, String> passwordDto) {
        String newPassword = passwordDto.get("newPassword");
        try {
            userService.changePassword(email, newPassword);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @PutMapping("/user/preferences")
    public ResponseEntity<User> updateUserPreferences(@RequestParam String email, @RequestBody String preferences) {
        return userService.findUserByEmail(email)
                .map(user -> {
                    user.setPreferences(preferences);
                    userService.registerUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
