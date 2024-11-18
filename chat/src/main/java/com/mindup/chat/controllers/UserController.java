package com.mindup.chat.controllers;

import com.mindup.chat.entities.User;
import com.mindup.chat.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}/availability")
    public ResponseEntity<?> toggleAvailability(@PathVariable String id) {
        // Llamar al servicio para cambiar el estado de disponibilidad
        User updatedUser = userService.toggleAvailability(id);

        // Retornar respuesta exitosa con el usuario actualizado
        return ResponseEntity.ok(updatedUser);
    }
}