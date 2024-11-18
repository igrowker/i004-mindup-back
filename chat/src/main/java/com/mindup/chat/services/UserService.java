package com.mindup.chat.services;

import com.mindup.chat.entities.User;

public interface UserService {
    User toggleAvailability(String id); // Alterna el estado de disponibilidad
}
