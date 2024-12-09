package com.mindup.core.services.IMPL;

import com.mindup.core.validations.VideoValidation;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mindup.core.mappers.UserMapper;
import com.mindup.core.validations.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
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

        UserValidation.validateUserRegister(userRegisterDTO);
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
    public Optional<UserProfileDTO> findUserById(String userId) {
        return userRepository.findById(userId)
                .map(this::mapToUserProfileDTO);
    }

    private UserProfileDTO mapToUserProfileDTO(User user) {
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setName(user.getName());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setRole(user.getRole());
        profileDTO.setBirth(user.getBirth());
        profileDTO.setAge(user.getAge());
        profileDTO.setPhone(user.getPhone());
        profileDTO.setLocation(user.getLocation());
        profileDTO.setGender(user.getGender());
        profileDTO.setInformation(user.getInformation());
        profileDTO.setChosenPsychologist(user.getChosenPsychologist());
        profileDTO.setImage(user.getImage());

        if (user.getRole() == Role.PSYCHOLOGIST) {
            profileDTO.setTuition(user.getTuition());
            profileDTO.setSpecialty(user.getSpecialty());
            profileDTO.setVideo(user.getVideo());
        }
        return profileDTO;
    }

    @Override
    @Transactional
    public void changePassword(String userId, String currentPassword, String newPassword) {
        System.out.println("Service invoked for userId: " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        System.out.println("User found: " + user.getEmail());
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("The current password is incorrect.");
        }
        PasswordValidation.validatePassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        System.out.println("Password updated successfully.");
    }

    @Override
    @Transactional
    public ResponseLoginDto authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return new ResponseLoginDto(null, email, null, null, null, null);
        }

        User user = userOptional.get();
        boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPassword());
        if (!isPasswordCorrect) {
            return new ResponseLoginDto(user.getUserId(), email, user.getName(), null, null, user.getRole().toString());
        }

        String token = jwtService.generateToken(email, user.getUserId(), user.getName(), user.getImage(), user.getRole().toString());
        return new ResponseLoginDto(user.getUserId(), email, user.getName(), null, user.getRole().toString(), token);
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
        ImageValidation.validateimage(profileImageDTO.getImage());
        user.setImage(profileImageDTO.getImage());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteProfileImage(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));
        user.setImage(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO toggleAvailability(String id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        user.setAvailability(!user.getAvailability());
        var savedUser = userRepository.save(user);
        if (user.getAvailability()) {
            chatFeignClient.joinProfessional(id);
        }
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
    public Boolean findProfessionalByUserIdAndRole(String id) {
        userRepository.findUserByUserIdAndRole(id, Role.PSYCHOLOGIST)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return true;
    }

    @Override
    public Boolean findPatientByUserIdAndRole(String id) {
        userRepository.findUserByUserIdAndRole(id, Role.PATIENT)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return true;
    }

    @Override
    @Transactional
    public void updateUserProfile(String userId, String name, UserProfileDTO userProfileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + name));

        boolean isUpdated = false;

        if (userProfileDTO.getName() != null) {
            if (!UserValidation.isValidName(userProfileDTO.getName())) {
                throw new InvalidInputException("Invalid name format.");
            }
            user.setName(userProfileDTO.getName());
            isUpdated = true;
        }

        if (userProfileDTO.getBirth() != null) {
            if (!UserValidation.isValidBirth(userProfileDTO.getBirth())) {
                throw new InvalidInputException("Invalid birth date.");
            }
            user.setBirth(userProfileDTO.getBirth());
            isUpdated = true;
        } else if (userProfileDTO.getBirth() == null && user.getBirth() != null) {
            throw new BirthUpdateException("Failed to update birth date for user with ID: " + name);
        }

        if (userProfileDTO.getGender() != null) {
            if (!UserValidation.isValidGender(userProfileDTO.getGender())) {
                throw new IllegalArgumentException("Invalid gender. It must be a valid Gender.");
            }
            user.setGender(userProfileDTO.getGender());
            isUpdated = true;
        }

        if (userProfileDTO.getPhone() != null) {
            UserValidation.validateUserProfile(userProfileDTO);
            user.setPhone(userProfileDTO.getPhone());
            isUpdated = true;
        }

        if (userProfileDTO.getLocation() != null) {
            if (!UserValidation.isValidLocation(userProfileDTO.getLocation())) {
                throw new InvalidLocationException("Invalid location format.");
            }
            user.setLocation(userProfileDTO.getLocation());
            isUpdated = true;
        }

        if (user.getRole() == Role.PSYCHOLOGIST) {
            if (userProfileDTO.getTuition() != null) {
                if (!UserValidation.isValidTuition(userProfileDTO.getTuition())) {
                    throw new InvalidInputException("Invalid tuition format.");
                }
                user.setTuition(userProfileDTO.getTuition());
                isUpdated = true;
            }
            if (userProfileDTO.getSpecialty() != null) {
                if (!UserValidation.isValidSpecialty(userProfileDTO.getSpecialty())) {
                    throw new InvalidInputException("Invalid specialty format.");
                }
                user.setSpecialty(userProfileDTO.getSpecialty());
                isUpdated = true;
            }
        }

        if (userProfileDTO.getInformation() != null) {
            if (!UserValidation.isValidInformation(userProfileDTO.getInformation())) {
                throw new InvalidInputException("Invalid information format.");
            }
            user.setInformation(userProfileDTO.getInformation());
            isUpdated = true;
        } else if (userProfileDTO.getInformation() == null && user.getInformation() != null) {
            throw new InformationUpdateException("Failed to update 'About Me' for user with ID: " + name);
        }

        if (isUpdated) {
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> requestPasswordReset(String email) {
        deleteExpiredTokens();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No user found with this email address: " + email));

        Optional<PasswordResetToken> existingTokenOpt = passwordResetTokenRepository.findByUser(user);
        if (existingTokenOpt.isPresent()) {
            PasswordResetToken existingToken = existingTokenOpt.get();
            if (existingToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("A password reset has already been requested."
                                + " Please wait for the token to expire before requesting another one.");
            } else {
                passwordResetTokenRepository.delete(existingToken);
            }
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(15);

        PasswordResetToken newToken = new PasswordResetToken();
        newToken.setUser(user);
        newToken.setToken(token);
        newToken.setExpiryDate(expirationDate);

        emailVerificationService.sendPasswordResetEmail(user.getEmail(), token);

        return ResponseEntity.ok("An email has been sent to reset your password.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElse(null);

        if (passwordResetToken == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token has expired");
        }

        try {
            PasswordValidation.validatePassword(newPassword);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
        return ResponseEntity.ok("Password has been reset successfully.");
    }

    private void deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<PasswordResetToken> expiredTokens = passwordResetTokenRepository.findAllByExpiryDateBefore(now);
        if (!expiredTokens.isEmpty()) {
            passwordResetTokenRepository.deleteAll(expiredTokens);
        }
    }

    @Override
    @Transactional
    public void updateProfileVideo(String userId, ProfileVideoDTO profileVideoDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));

        if (user.getRole() != Role.PSYCHOLOGIST) {
            throw new IllegalArgumentException("Only users with role PSYCHOLOGIST can have a video.");
        }

        VideoValidation.validateVideo(profileVideoDTO.getVideo());
        user.setVideo(profileVideoDTO.getVideo());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteProfileVideo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));

        if (user.getRole() != Role.PSYCHOLOGIST) {
            throw new IllegalArgumentException("Only users with role PSYCHOLOGIST can have a video.");
        }

        user.setVideo(null);
        userRepository.save(user);
    }
}
