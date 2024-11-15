package com.mindup.core.services;

import com.mindup.core.entities.User;
import com.mindup.core.enums.Role;
import com.mindup.core.exceptions.UserAlreadyExistsException;
import com.mindup.core.exceptions.UserNotFoundException;
import com.mindup.core.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String name, String email, String password, Role role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("The email " + email + " is already in use.");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void changePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean authenticateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }
}
