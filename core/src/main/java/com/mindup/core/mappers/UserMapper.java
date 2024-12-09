package com.mindup.core.mappers;
import com.mindup.core.dtos.User.UserDTO;
import com.mindup.core.dtos.User.UserProfileDTO;
import com.mindup.core.dtos.User.UserRegisterDTO;
import org.mapstruct.Mapper;
import com.mindup.core.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterDTO dto);
    UserDTO toUserDTO(User user);
    UserProfileDTO toUserProfileDTO(User user);
}
