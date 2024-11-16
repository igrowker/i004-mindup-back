package com.mindup.core.dtos;

import com.mindup.core.enums.Role;
import lombok.Data;

@Data
public class UserRegisterDTO {

    private String name;
    private String email;
    private Role role;
    private String password;
    private String confirmPassword;
}
