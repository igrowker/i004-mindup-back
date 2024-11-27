package com.mindup.chat.repositories;

import com.mindup.chat.entities.AvailablePsychologists;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AvailablePsychologistsRepository extends MongoRepository<AvailablePsychologists, String> {
}
