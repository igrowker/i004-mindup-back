package com.mindup.chat.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mindup.chat.entities.User;

public interface UserRepository extends MongoRepository<User, String> {
    
}
