package com.mindup.chat.services.implementations;

import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.entities.TemporalChat;
import com.mindup.chat.repositories.TemporalChatRepository;
import com.mindup.chat.services.MessageService;
import com.mindup.chat.utils.Scraper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final Scraper scraper;
    private final TemporalChatRepository temporalChatRepository;


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
