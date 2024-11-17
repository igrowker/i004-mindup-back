package com.mindup.chat.controllers;
import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.entities.Message;
import com.mindup.chat.repositories.MessageRepository;
import com.mindup.chat.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;
    private final MessageService messageService;

    @PostMapping("/test")
    public ResponseEntity<?> addAmount(){
        messageRepository.save(new Message());
        return ResponseEntity.ok(true);
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
    //url ("/api/message/send") debido al request mapping
    @PostMapping("/send")
    public void sendMessage(@RequestBody Message message){
        simpMessagingTemplate.convertAndSend("/topic/messages",message);
    }


}


//POST /chat/send: Envía un mensaje en el chat de emergencia.
//GET /emergency-contact: Proporciona información de contacto de emergencia si no hay profesionales disponibles.