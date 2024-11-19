package com.mindup.core.dtos.User;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserLoginDTO {

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
