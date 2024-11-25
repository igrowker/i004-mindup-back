package com.mindup.chat.mappers;

import com.mindup.chat.dtos.TemporalChatIdDto;
import com.mindup.chat.entities.TemporalChat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemporalChatMapper {

    TemporalChatIdDto toTemporalChatDto(TemporalChat temporalChat);
    TemporalChat toTemporalChat(TemporalChatIdDto temporalChatIdDto);
}
