package com.mindup.chat.repositories;

import com.mindup.chat.entities.AvailablePsychologists;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AvailablePsychologistsRepository extends MongoRepository<AvailablePsychologists, String> {
    Optional<AvailablePsychologists> findByProfessionalId(String professionalId);
    boolean existsByProfessionalId(String professionalId);
    void deleteByProfessionalId(String professionalId);
}
