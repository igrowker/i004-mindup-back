package com.mindup.core.mappers;

import com.mindup.core.dtos.*;
import org.mapstruct.Mapper;
import com.mindup.core.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterDTO dto);

    UserDTO toUserDTO(User user);
}
