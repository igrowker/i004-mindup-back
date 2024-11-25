package com.mindup.chat.services;

import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.dtos.TemporalChatIdDto;

import java.io.IOException;
import java.util.List;

public interface MessageService {

    void joinProfessional(String professionalId);
    Boolean professionalAccepted(TemporalChatIdDto temporalChatIdDto);
    TemporalChatIdDto requestChat(String patientId);
    List<ResponseEmergencyDto> getEmergencyContact() throws IOException;
    List<ResponseOtherResourcesDto> getOtherResources() throws IOException;
}
