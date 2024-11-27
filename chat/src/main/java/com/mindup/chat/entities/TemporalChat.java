package com.mindup.chat.entities;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "temporal_chat")
public class TemporalChat {

    @Id
    private String temporalChatId = UUID.randomUUID().toString();
    private String patientId;
    private String professionalId;
    private LocalDateTime timestamp=LocalDateTime.now();

}

