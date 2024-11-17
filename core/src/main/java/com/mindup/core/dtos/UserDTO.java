package com.mindup.core.dtos;

import com.mindup.core.enums.Role;
import lombok.Data;

@Data
public class UserDTO {

    private String name;
    private String email;
    private Role role;
    private String preferences;
    private String profile;

    public UserDTO(String name, String email, Role role, String preferences, String profile) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.preferences = preferences;
        this.profile = profile;
    }
}
