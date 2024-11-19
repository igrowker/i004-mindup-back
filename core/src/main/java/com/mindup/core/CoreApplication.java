package com.mindup.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class CoreApplication {

    public static void main(String[] args) {
        // Cargar variables del .env
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(CoreApplication.class, args);
    }
}