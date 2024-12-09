package com.mindup.chat.repositories;
import com.mindup.chat.entities.Message;
import com.mindup.chat.entities.TemporalChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TemporalChatRepository extends MongoRepository<TemporalChat, String> {
    List<Message> findByPatientId(String senderId);
    List<Message> findByProfessionalId(String receiverId);

}
