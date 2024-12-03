package com.mindup.chat.services.implementations;

import com.mindup.chat.dtos.RequestMessageDto;
import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.dtos.TemporalChatDto;
import com.mindup.chat.entities.AvailablePsychologists;
import com.mindup.chat.entities.Message;
import com.mindup.chat.entities.TemporalChat;
import com.mindup.chat.exceptions.ResourceNotFoundException;
import com.mindup.chat.feign.CoreFeignClient;
import com.mindup.chat.mappers.MessageMapper;
import com.mindup.chat.mappers.TemporalChatMapper;
import com.mindup.chat.repositories.AvailablePsychologistsRepository;
import com.mindup.chat.repositories.MessageRepository;
import com.mindup.chat.repositories.TemporalChatRepository;
import com.mindup.chat.services.MessageService;
import com.mindup.chat.utils.Scraper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final Scraper scraper;
    private final TemporalChatRepository temporalChatRepository;
    private final Queue<String> professionalQueue = new LinkedList<>();
    private final AvailablePsychologistsRepository availablePsychologistsRepository;
    private final TemporalChatMapper temporalChatMapper;
    private final CoreFeignClient coreFeignClient;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    // Endpoint 1 (viene desde backend, api/core/user/availability/{professionalId})
    @Transactional
    @Override
    public void joinProfessional(String professionalId) throws IOException {

        AvailablePsychologists professional = new AvailablePsychologists();

        if(availablePsychologistsRepository.existsByProfessionalId(professionalId)){
            availablePsychologistsRepository.deleteByProfessionalId(professionalId);
        }
        professional.setProfessionalId(professionalId);
        professional.setTimestamp(LocalDateTime.now());
        availablePsychologistsRepository.save(professional);
        professionalQueue.add(professionalId);
    }

    //Endpoint 2: Setea un temporalChat y le pasa a front las ids antes de dejar la de professional nula (por el scheduler). Deshabilita al profesional SIEMPRE
    // Porque: A. si no responde o cancela, debe ser deshabilitado y B. si acepta, no debe ser interrumpido con otro pedido de chat.
    @Transactional
    @Override
    public TemporalChatDto requestChat(String patientId) throws IOException {
        try {
            coreFeignClient.findPatientByUserIdAndRole(patientId);//si no pongo el runtimeexception no lo toma
        }catch (RuntimeException e){ //agregar exception que no es el mismo usuario el que hace la petixion con el id
            throw new ResourceNotFoundException("No existe el id del usuario.");
        }
        TemporalChat temporalChat = new TemporalChat();
        temporalChat.setPatientId(patientId);
        AvailablePsychologists professional;
        try {
            professional = availablePsychologistsRepository.findAll().get(0);
        } catch (RuntimeException e) {
            var numbers = scraper.getEmergencyContactList();
            throw new ResourceNotFoundException("There's no available psychologist. For further help you can call to these numbers: " + numbers);
        }
        temporalChat.setProfessionalId(professional.getProfessionalId());
        TemporalChat temporalChat1=temporalChatRepository.save(temporalChat);
        TemporalChatDto temporalChatDto =temporalChatMapper.toTemporalChatDto(temporalChat1);
        temporalChat1.setProfessionalId(null);
        coreFeignClient.toggleAvailability(professional.getProfessionalId());
        availablePsychologistsRepository.delete(professional);
        temporalChatRepository.save(temporalChat1);
        return temporalChatDto;
    }

    //Endpoint 3: Corrobora que sea profesional y settea su id en el temporalChatDb. Esto sirve para pasar luego a la DB permanente (scheduler).
    @Transactional
    @Override
    public Boolean professionalAccepted(TemporalChatDto temporalChatDto) {
        try {
            coreFeignClient.findProfessionalByUserIdAndRole(temporalChatDto.professionalId());
        }catch (RuntimeException e){ //agregar exception que no es el mismo usuario el que hace la petixion con el id
            throw new ResourceNotFoundException("No existe el id del usuario.");
        }
        TemporalChat temporalChat = temporalChatRepository.findById(temporalChatDto.temporalChatId())
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + temporalChatDto.temporalChatId()));
        temporalChat.setProfessionalId(temporalChatDto.professionalId());
        temporalChatRepository.save(temporalChat);
        return true;
    }

    //Endpoint 4: Recupera el mensaje, setea los datos que front no tiene y guarda la entidad en la db histórica.
    @Transactional
    @Override
    public void sendToOtherUser(RequestMessageDto requestMessageDto, String professionalId) {
        Message entity = messageMapper.messageDtoToEntity(requestMessageDto);
        entity.setProfessionalId(professionalId);
        entity.setTimestamp(LocalDateTime.now());
       messageRepository.save(entity);
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
