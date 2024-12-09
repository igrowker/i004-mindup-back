package com.mindup.chat.services;

import com.mindup.chat.dtos.RequestMessageDto;
import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.dtos.TemporalChatDto;

import java.io.IOException;
import java.util.List;

public interface MessageService {

    void joinProfessional(String professionalId) throws IOException;
    Boolean professionalAccepted(TemporalChatDto temporalChatDto);
    TemporalChatDto requestChat(String patientId) throws IOException;
    void sendToOtherUser(RequestMessageDto requestMessageDto, String professionalId);
    List<ResponseEmergencyDto> getEmergencyContact() throws IOException;
    List<ResponseOtherResourcesDto> getOtherResources() throws IOException;
}
