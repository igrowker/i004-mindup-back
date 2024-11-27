package com.mindup.chat.mappers;

import com.mindup.chat.dtos.TemporalChatDto;
import com.mindup.chat.entities.TemporalChat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemporalChatMapper {

    TemporalChatDto toTemporalChatDto(TemporalChat temporalChat);
    TemporalChat toTemporalChat(TemporalChatDto temporalChatDto);
}
