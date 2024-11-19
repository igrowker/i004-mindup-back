package com.mindup.chat.entities;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {

    @Id
    private String messageId;

    private String senderId;
    private String receiverId;

    @Size(max = 1000)
    private String content;

    private LocalDateTime timestamp = LocalDateTime.now();
}
