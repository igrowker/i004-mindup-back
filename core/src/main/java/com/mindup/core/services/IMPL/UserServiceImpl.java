package com.mindup.core.services.IMPL;

import com.mindup.core.entities.EmailVerification;
import com.mindup.core.dtos.User.*;
import com.mindup.core.entities.PasswordResetToken;
import com.mindup.core.entities.User;
import com.mindup.core.enums.*;
import com.mindup.core.exceptions.*;
import com.mindup.core.feign.ChatFeignClient;
import com.mindup.core.repositories.PasswordResetTokenRepository;
import com.mindup.core.repositories.UserRepository;
import com.mindup.core.security.JwtService;
import com.mindup.core.services.EmailVerificationService;
import com.mindup.core.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mindup.core.mappers.UserMapper;
import com.mindup.core.validations.*;
import java.time.LocalDateTime;
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
    private final ChatFeignClient chatFeignClient;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    @Transactional
    public UserDTO registerUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findByEmail(userRegisterDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("The email " + userRegisterDTO.getEmail() + " is already in use.");
        }

        PasswordValidation.validatePassword(userRegisterDTO.getPassword());

        User user = userMapper.toUser(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setUser(user);
        emailVerification.setVerificationToken(token);
        emailVerification.setVerified(false);
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
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return new ResponseLoginDto(null, email,"Account not found");
        }

        User user = userOptional.get();
        boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPassword());
        if (!isPasswordCorrect) {
            return new ResponseLoginDto(user.getUserId(), email,"Invalid mail or password");
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

    @Override
    @Transactional
    public void deleteUserAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void updateProfileImage(String userId, ProfileImageDTO profileImageDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));

        ProfileImageValidation.validateProfileImageUrl(profileImageDTO.getProfileImageUrl());

        user.setProfileImageUrl(profileImageDTO.getProfileImageUrl());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteProfileImage(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));

        user.setProfileImageUrl(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO toggleAvailability(String id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        user.setAvailability(!user.getAvailability());
        var savedUser = userRepository.save(user);
        if (user.getAvailability()){ chatFeignClient.subscribeProfessional(id); }
        var response = userMapper.toUserDTO(savedUser);
        return response;
    }

    @Transactional
    public UserProfileDTO getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return userMapper.toUserProfileDTO(user);
    }

    @Override
    @Transactional
    public void updateUserProfile(String userId, UserProfileDTO userProfileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        if (userProfileDTO.getName() != null) {
            user.setName(userProfileDTO.getName());
        }
        if (userProfileDTO.getBirthDate() != null) {
            user.setBirthDate(userProfileDTO.getBirthDate());
        }
        if (userProfileDTO.getGender() != null) {
            user.setGender(userProfileDTO.getGender());
        }
        if (userProfileDTO.getPhone() != null) {
            UserValidation.validateUserProfile(userProfileDTO);
            user.setPhone(userProfileDTO.getPhone());
        }
        if (user.getRole() == Role.PSYCHOLOGIST) {
            if (userProfileDTO.getTuition() != null) {
                user.setTuition(userProfileDTO.getTuition());
            }
            if (userProfileDTO.getSpecialty() != null) {
                user.setSpecialty(userProfileDTO.getSpecialty());
            }
        }
        if (userProfileDTO.getAboutMe() != null) {
            user.setAboutMe(userProfileDTO.getAboutMe());
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con el email: " + email));

        String token;
        boolean tokenExists;

        do {
            token = UUID.randomUUID().toString();
            tokenExists = passwordResetTokenRepository.findByToken(token).isPresent();
        } while (tokenExists);

        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1);
        Optional<PasswordResetToken> existingTokenOpt = passwordResetTokenRepository.findByUser (user);

        if (existingTokenOpt.isPresent()) {
            PasswordResetToken existingToken = existingTokenOpt.get();

            if (existingToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                passwordResetTokenRepository.delete(existingToken);
            } else {
                emailVerificationService.sendPasswordResetEmail(user.getEmail(), existingToken.getToken());
                return;
            }
        }

        PasswordResetToken newToken = new PasswordResetToken();
        newToken.setUser (user);
        newToken.setToken(token);
        newToken.setExpiryDate(expirationDate);
        passwordResetTokenRepository.save(newToken);
        emailVerificationService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }
        User user = passwordResetToken.getUser ();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
    }
}
