package com.mindup.chat.controllers;
import com.mindup.chat.entities.Message;
import com.mindup.chat.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageRepository messageRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addAmount(){
        messageRepository.save(new Message());
        return ResponseEntity.ok(true);
    }
}
