package com.mindup.chat.controllers;

import com.mindup.chat.dtos.RequestMessageDto;
import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.dtos.TemporalChatDto;
import com.mindup.chat.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;


    //Endpoint 1: Profesional fue cambiado a DISPONIBLE. Aquí lo agregamos a la tabla de availableProfessionals y enviamos mensaje genérico de ok (@payload).
    @PostMapping("/join-professional/{professionalId}")
    public ResponseEntity<?> joinProfessional(@PathVariable String professionalId) throws IOException {
        messageService.joinProfessional(professionalId);
        return ResponseEntity.ok().build();
    }

    //Endpoint 2: Setea un temporalChat y le pasa a front las ids antes de dejar la de professional nula (por el scheduler).
    @GetMapping("/request-chat/{patientId}")
    public ResponseEntity<TemporalChatDto>requestChat(@PathVariable String patientId) throws IOException {
        TemporalChatDto temporalChatDto =messageService.requestChat(patientId);
        messagingTemplate.convertAndSendToUser(temporalChatDto.professionalId(), "/queue/notifications/" + temporalChatDto.professionalId(),
                "Una persona solicitó chat de emergencia. ¿Estás disponible para atenderla?");
        return ResponseEntity.ok(temporalChatDto);
    }

    //Endpoint 3: Corrobora que sea profesional y settea su id en el temporalChatDb. Esto sirve para pasar luego a la DB permanente (scheduler).
    @PostMapping("/professional-accepted")
    public ResponseEntity<Boolean> professionalAccepted(@RequestBody @Valid TemporalChatDto temporalChatDto){
        var flag=messageService.professionalAccepted(temporalChatDto);
        return ResponseEntity.ok(flag);
    }

    //Endpoint 4: Recupera el mensaje, setea los datos que front no tiene y guarda la entidad en la db histórica. Luego WS convierte y envía el mensaje aclarando el emisor.
    @MessageMapping("/user-message/{professionalId}")
    public void sendToOtherUser(@Payload RequestMessageDto requestMessageDto, @DestinationVariable String professionalId, @Header("simpSessionId") String sessionId) {
        messageService.sendToOtherUser(requestMessageDto, professionalId);
        messagingTemplate.convertAndSend("/queue/reply-" + professionalId, "Mensaje de: " + requestMessageDto.sender() + ": " + requestMessageDto.content() );
    }

    @GetMapping("/emergency-contact")
    public ResponseEntity<List<ResponseEmergencyDto>> emergencyContact() throws IOException {
        List<ResponseEmergencyDto> emergencyContact = messageService.getEmergencyContact();
        return ResponseEntity.ok(emergencyContact);
    }

    @GetMapping("/other-resources")
    public ResponseEntity<List<ResponseOtherResourcesDto>> otherResources() throws IOException {
        List<ResponseOtherResourcesDto> otherResources = messageService.getOtherResources();
        return ResponseEntity.ok(otherResources);
    }
    


}
