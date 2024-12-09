package com.mindup.chat.repositories;
import com.mindup.chat.entities.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findByPatientId(String senderId);

    List<Message> findByProfessionalId(String receiverId);

}
