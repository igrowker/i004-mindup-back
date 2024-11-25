package com.mindup.chat.services.implementations;

import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.dtos.TemporalChatIdDto;
import com.mindup.chat.entities.TemporalChat;
import com.mindup.chat.exceptions.ResourceNotFoundException;
import com.mindup.chat.feign.CoreFeignClient;
import com.mindup.chat.mappers.TemporalChatMapper;
import com.mindup.chat.repositories.AvailablePsychologistsRepository;
import com.mindup.chat.repositories.TemporalChatRepository;
import com.mindup.chat.services.MessageService;
import com.mindup.chat.utils.Scraper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final AvailablePsychologistsRepository availablePsychologistsRepository;
    private final TemporalChatMapper temporalChatMapper;
    private final CoreFeignClient coreFeignClient;


    @Override
    public void joinProfessional(String professionalId) {
        // comprobar que es un profesional (id valido)
        professionalQueue.add(professionalId);
        simpMessagingTemplate.convertAndSendToUser(professionalId, "/queue/notifications", "Ahora estas disponible para la asistencia por chat.");
    }

    @Override
    public Boolean professionalAccepted(TemporalChatIdDto temporalChatIdDto) {
        //todo. corroborar con userDB feignclient
        coreFeignClient.findProfessionalById(temporalChatIdDto.professionalId());
        //GUILLEEEEEE NOS QUEDAMOS ACAAAAAA
        //availablePsychologistsRepository.delete(availablePsychologist);
        return true;
    }

    @Transactional
    @Override
    public TemporalChatIdDto requestChat(String patientId) {
        TemporalChat temporalChat = new TemporalChat(); //validar id de patient
        temporalChat.setPatientId(patientId);

        var professionalId = availablePsychologistsRepository.findAll().get(0);
        temporalChat.setProfessionalId(professionalId.getProfessionalId());
        TemporalChat temporalChat1=temporalChatRepository.save(temporalChat);
        TemporalChatIdDto temporalChatIdDto=temporalChatMapper.toTemporalChatDto(temporalChat1);
        temporalChat1.setProfessionalId(null);
        temporalChatRepository.save(temporalChat1);
        return temporalChatIdDto;
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
