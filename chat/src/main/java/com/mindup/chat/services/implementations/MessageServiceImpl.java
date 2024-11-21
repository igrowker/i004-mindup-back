package com.mindup.chat.services.implementations;

import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.entities.Message;
import com.mindup.chat.entities.TemporalChat;
import com.mindup.chat.repositories.TemporalChatRepository;
import com.mindup.chat.services.MessageService;
import com.mindup.chat.utils.Scraper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final Scraper scraper;
    private final TemporalChatRepository temporalChatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Queue<String> professionalQueue = new LinkedList<>();


    @Override
    public void joinProfessional(String professionalId) {
        // comprobar que es un profesional (id valido)
        professionalQueue.add(professionalId);
        simpMessagingTemplate.convertAndSendToUser(professionalId, "/queue/notifications", "Ahora estas disponible para la asistencia por chat.");
    }
/*
    @Override
    public void sendMessage(MessageDTO message) {

    }

    @Override
    public void createRoom(RoomDTO roomDTO) {

    }*/

    @Override
    public void sendMessage(Message message) {

    }

    @Transactional
    @Override
    public boolean requestChat(String patientId) {
        TemporalChat temporalChat = new TemporalChat();
        temporalChat.setPatientId(patientId);
        try {
            temporalChatRepository.save(temporalChat);
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ResponseEmergencyDto> getEmergencyContact() throws IOException {
        return scraper.getEmergencyContactList();
    }

    @Override
    public List<ResponseOtherResourcesDto> getOtherResources() throws IOException {
        return scraper.getOtherResourcesList();
    }
}
