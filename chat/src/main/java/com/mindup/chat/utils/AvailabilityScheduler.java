package com.mindup.chat.utils;
import com.mindup.chat.entities.Message;
import com.mindup.chat.entities.TemporalChat;
import com.mindup.chat.repositories.MessageRepository;
import com.mindup.chat.repositories.TemporalChatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@RequiredArgsConstructor
@Service
public class AvailabilityScheduler {

    private final TemporalChatRepository temporalChatRepository;
    private final MessageRepository messageRepository;

    @Transactional
    @Scheduled(cron = "0 * * * * ?", zone = "America/Argentina/Buenos_Aires")
    public void checkAndTurnProfessionalUnavailable() {
        List<TemporalChat> allRegisters = temporalChatRepository.findAll();
        // Si no hay psicos disponibles en el topic de Socket, responder de una números de emergencia.
        for (TemporalChat register : allRegisters) {
            if (register.getTimestamp().plus(1, ChronoUnit.MINUTES).isBefore(LocalDateTime.now())
                    && register.getProfessionalId() == null) {
                temporalChatRepository.delete(register);
                //Buscar el psicólogo que correspondería que tome el llamado y pasarlo a no disponible.
                //Buscar al próximo psicólogo.
            }
            Message message = new Message();
            message.setPatientId(register.getPatientId());
            message.setProfessionalId(register.getProfessionalId());
            message.setSender(register.getPatientId());
            message.setTimestamp(LocalDateTime.now());
            message.setContent("Nuevo chat iniciado con éxito a las " + message.getTimestamp() + " entre paciente "
                    + message.getPatientId() + " y profesional " + message.getProfessionalId());
            messageRepository.save(message);
        }
    }


    @Transactional
    @Scheduled(cron = "0 0 * * * ?", zone = "America/Argentina/Buenos_Aires")
    public void checkAndDeleteOldData() {
        List<TemporalChat> allRegisters = temporalChatRepository.findAll();
        for (TemporalChat register : allRegisters) {
            if (register.getTimestamp().plus(60, ChronoUnit.MINUTES).isBefore(LocalDateTime.now())) {
                temporalChatRepository.delete(register);
            }
        }
    }
}
