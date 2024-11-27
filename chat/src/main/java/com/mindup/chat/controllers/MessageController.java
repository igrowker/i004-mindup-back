package com.mindup.chat.controllers;

import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.services.AvailablePsychologistsService;
import com.mindup.chat.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AvailablePsychologistsService availablePsychologistsService;

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
    public boolean subscribeProfessional(@PathVariable String id) {
        boolean response = availablePsychologistsService.subscribeProfessional(id);
        return true;
    }

    @GetMapping("/find-professional")
    public String findFirstProfessional() {
        String id = availablePsychologistsService.findFirstProfessional();
        return id;
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