package com.mindup.core.services.IMPL;

import com.mindup.core.dtos.User.ResponseLoginDto;
import com.mindup.core.dtos.User.UserDTO;
import com.mindup.core.dtos.User.UserRegisterDTO;
import com.mindup.core.entities.EmailVerification;
import com.mindup.core.entities.User;
import com.mindup.core.exceptions.*;
import com.mindup.core.repositories.EmailVerificationRepository;
import com.mindup.core.repositories.UserRepository;
import com.mindup.core.security.JwtService;
import com.mindup.core.services.EmailVerificationService;
import com.mindup.core.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mindup.core.mappers.UserMapper;
import com.mindup.core.validations.PasswordValidation;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService { 

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    @Transactional
    public UserDTO registerUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findByEmail(userRegisterDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("The email " + userRegisterDTO.getEmail() + " is already in use.");
        }

        PasswordValidation.validatePassword(userRegisterDTO.getPassword());

        User user = userMapper.toUser (userRegisterDTO);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setUser (user);
        emailVerification.setVerificationToken(token);
        emailVerification.setVerified(false);
        emailVerificationRepository.save(emailVerification);

        emailVerificationService.sendVerificationEmail(user.getEmail(), token);

        return userMapper.toUserDTO(user);
    }

    @Override
    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toUserDTO);
    }

    @Override
    @Transactional
    public void changePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public ResponseLoginDto authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPassword());
        if (!isPasswordCorrect) {
            throw new RuntimeException("Invalid mail or password");
        }
        String token = jwtService.generateToken(email);
        return new ResponseLoginDto(user.getUserId(), email, token);
    }

    @Override
    @Transactional
    public void updateUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userDTO.getEmail()));
        user.setPreferences(userDTO.getPreferences());
        userRepository.save(user);
    }
}
