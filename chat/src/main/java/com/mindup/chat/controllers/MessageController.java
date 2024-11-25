package com.mindup.chat.controllers;

import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.dtos.TemporalChatIdDto;
import com.mindup.chat.entities.Message;
import com.mindup.chat.services.AvailablePsychologistsService;
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
import java.util.LinkedList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final Queue<String> professionalQueue = new LinkedList<>();
    private final AvailablePsychologistsService availablePsychologistsService;


    @PostMapping("/join/{professionalId}")
    public ResponseEntity<?> joinProfessional(@PathVariable String professionalId) {
        messageService.joinProfessional(professionalId);
        return ResponseEntity.ok().build();//devolxer id de ws, sino es el mismo que usuario
    }
    

    @MessageMapping("/broadcast")
    @SendTo("/topic/reply")
    public String broadcastMessage(@Payload String message) {
        return LocalDateTime.now() + " - " + "You have received a message: " + message;
    }

    @MessageMapping("/user-message-{userName}")
    public void sendToOtherUser(@Payload String message, @DestinationVariable String userName, @Header("simpSessionId") String sessionId) {
        messagingTemplate.convertAndSend("/queue/reply-" + userName, "You have a message from someone: " + message);
    }

    @PostMapping("/subscribe-professional/{id}")
    public ResponseEntity<Boolean> subscribeProfessional(@PathVariable String id) throws IOException {
        Boolean response = availablePsychologistsService.subscribeProfessional(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-professional")
    public ResponseEntity<TemporalChatIdDto> findFirstProfessional() {
        TemporalChatIdDto temporalChatIdDto = availablePsychologistsService.findFirstProfessional();
        return ResponseEntity.ok(temporalChatIdDto);
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
    
    @PostMapping("/professional-accepted")
    public ResponseEntity<Boolean> professionalAccepted(@RequestBody @Valid TemporalChatIdDto temporalChatIdDto){
        var flag=messageService.professionalAccepted(temporalChatIdDto);
        return ResponseEntity.ok(flag);
    }

    @GetMapping
            ("/request-chat/{patientId}")
    public ResponseEntity<?> requestChat(@PathVariable String patientId){
        TemporalChatIdDto temporalChatIdDto=messageService.requestChat(patientId);
        return ResponseEntity.ok(temporalChatIdDto);
    }
}
