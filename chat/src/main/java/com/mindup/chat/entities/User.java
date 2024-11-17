package com.mindup.chat.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users") // Define la colección en MongoDB
public class User {

    @Id // Identificador único
    private String id; // MongoDB utiliza un String como ID por defecto

    private String name;
    private String email;
    private boolean available; 
}
