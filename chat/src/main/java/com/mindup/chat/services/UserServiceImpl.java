package com.mindup.chat.services;

import com.mindup.chat.entities.User;
import com.mindup.chat.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User toggleAvailability(String id) {
        // Buscar el usuario por ID
        User user = userRepository.findById(id).orElseThrow(() -> 
            new IllegalArgumentException("Usuario no encontrado con ID: " + id));

        // Cambiar el estado de 'available' al opuesto
        user.setAvailable(!user.isAvailable());

        // Guardar el usuario actualizado
        return userRepository.save(user);
    }
}