package com.mindup.chat.entities;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {

    @Id
    private String messageId = UUID.randomUUID().toString();

    private String patientId;
    private String professionalId;
    private String sender;
    // Si las ids de WS no pueden igualarse a las de patient y professional de la db, deben agregarse acá.

    @Size(max = 1000)
    private String content;

    private LocalDateTime timestamp = LocalDateTime.now();
}
