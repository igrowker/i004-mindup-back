package com.mindup.core.mappers;

import com.mindup.core.dtos.UserDTO;
import com.mindup.core.dtos.UserRegisterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.mindup.core.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "preferences", ignore = true) 
    @Mapping(target = "profile", ignore = true)
    User toUser(UserRegisterDTO dto);

    UserDTO toUserDTO(User user);
}
