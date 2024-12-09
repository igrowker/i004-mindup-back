package com.mindup.core.dtos.User;

import com.mindup.core.enums.Gender;
import com.mindup.core.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private int age;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phone;

    private String location;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private String tuition;

    private String specialty;

    private String information;

    private String chosenPsychologist;

    private String image;

    private String video;
}
