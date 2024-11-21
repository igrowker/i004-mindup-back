package com.mindup.chat.services;

import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.entities.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

public interface MessageService {

    void joinProfessional(String professionalId);
    /*void sendMessage(MessageDTO message);
    void createRoom(RoomDTO roomDTO);*/
    void sendMessage(Message message);
    boolean requestChat(String patientId);
    List<ResponseEmergencyDto> getEmergencyContact() throws IOException;
    List<ResponseOtherResourcesDto> getOtherResources() throws IOException;
}
