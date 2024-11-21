package com.mindup.chat.controllers;
import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.entities.Message;
import com.mindup.chat.entities.TemporalChat;
import com.mindup.chat.repositories.MessageRepository;
import com.mindup.chat.repositories.TemporalChatRepository;
import com.mindup.chat.services.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final Queue<String> professionalQueue = new LinkedList<>();


    @PostMapping("/join/{professionalId}")
    public ResponseEntity<?> joinProfessional(@PathVariable String professionalId) {
        messageService.joinProfessional(professionalId);
        return ResponseEntity.ok().build();//devolxer id de ws, sino es el mismo que usuario
    }
/*
    @MessageMapping("/send")
    public void sendMessage(@Payload MessageDTO message) {
        String nextUser2 = user2Queue.poll(); // Toma al siguiente usuario tipo 2.

        if (nextUser2 != null) {
            simpMessagingTemplate.convertAndSendToUser(nextUser2, "/queue/messages", message);
            user2Queue.add(nextUser2); // Lo regresa al final de la cola.
        } else {
            simpMessagingTemplate.convertAndSend("/queue/errors", "No hay usuarios disponibles.");
        }
    }

    @MessageMapping("/create-room")
    public void createRoom(@Payload RoomDTO roomDTO) {
        String roomTopic = "/topic/room/" + roomDTO.roomId();
        simpMessagingTemplate.convertAndSend(roomTopic, "Sala creada entre " + roomDTO.user1() + " y " + roomDTO.user2());
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody Message message) {
        if (!user2Queue.isEmpty()) {
            String nextUser2 = user2Queue.peek(); // Obtiene el siguiente usuario sin removerlo.
            simpMessagingTemplate.convertAndSendToUser(nextUser2, "/queue/messages", message);
        } else {
            simpMessagingTemplate.convertAndSend("/queue/errors", "No hay usuarios disponibles para recibir el mensaje.");
        }
    }

*/

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
        //if Front-end wants to recive the message, it will need to suscribe the user to ("/topic/messages")
        simpMessagingTemplate.convertAndSend("/topic/messages",message);//sending messages to all subscribers. comments 
    }


}

