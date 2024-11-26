package com.mindup.chat.mappers;

import com.mindup.chat.dtos.RequestMessageDto;
import com.mindup.chat.entities.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message messageDtoToEntity(RequestMessageDto requestMessageDto);
}
