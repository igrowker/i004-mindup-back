package com.mindup.core.dtos.User;

import com.mindup.core.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private Role role;
    private String preferences;
    private String profile;
}
