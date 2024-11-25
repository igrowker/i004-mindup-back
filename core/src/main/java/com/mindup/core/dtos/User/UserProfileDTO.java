package com.mindup.core.dtos.User;

import com.mindup.core.enums.Gender;
import com.mindup.core.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Role is required")
    private Role role;

    private LocalDate birthDate;

    private int age;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phone;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private String tuition;

    private String specialty;

    private String aboutMe;
}
