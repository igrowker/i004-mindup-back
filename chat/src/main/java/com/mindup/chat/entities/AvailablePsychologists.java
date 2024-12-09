package com.mindup.chat.entities;

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
@Document(collection = "available_psychologists")
public class AvailablePsychologists {

    @Id
    private String availablePsychologistsId = UUID.randomUUID().toString();
    private String professionalId;
    private LocalDateTime timestamp = LocalDateTime.now();
}
