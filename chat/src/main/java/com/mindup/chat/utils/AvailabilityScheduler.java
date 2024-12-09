package com.mindup.chat.utils;
import com.mindup.chat.entities.AvailablePsychologists;
import com.mindup.chat.entities.Message;
import com.mindup.chat.entities.TemporalChat;
import com.mindup.chat.repositories.AvailablePsychologistsRepository;
import com.mindup.chat.repositories.MessageRepository;
import com.mindup.chat.repositories.TemporalChatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@RequiredArgsConstructor
@Service
public class AvailabilityScheduler {

    private final TemporalChatRepository temporalChatRepository;
    private final MessageRepository messageRepository;
    private final AvailablePsychologistsRepository availablePsychologistsRepository;
    private final Scraper scraper;


//Cada UN minuto, suponiendo que hay al menos UN psico disponible (en lista availablePsychologist) se inicia este método, que
    //1. corrobora si hay psicos disponibles (si no, pasa los números de emergencia). Y no continúa hasta que alguien más se pase a disponible.
    //2. por cada pedido de chat se corrobora si ya pasó al menos un minuto sin que se respondiera al pedido, entonces se borra ese registro del temporalChatDB.
    //3. si ya hay un psico asignado (es decir, aceptó el pedido), se pasa la info a la DB message para conservarlo y se borra el registro de la temporalChatDB.
    @Transactional
    @Scheduled(cron = "0 * * * * ?", zone = "America/Argentina/Buenos_Aires")
    public void checkAndTurnProfessionalUnavailable() throws IOException {

            List<TemporalChat> allRegisters = temporalChatRepository.findAll();
            List<AvailablePsychologists> availablePsychologists = availablePsychologistsRepository.findAll();

            //borramos numeros de emer
            for (TemporalChat register : allRegisters) {
                if (register.getTimestamp().plus(1, ChronoUnit.MINUTES).isBefore(LocalDateTime.now())
                        && register.getProfessionalId() == null) {
                    temporalChatRepository.delete(register);
                }
                System.out.println("no entre al if");
                if (register.getProfessionalId() != null) {
                    Message message = new Message();
                    message.setPatientId(register.getPatientId());
                    message.setProfessionalId(register.getProfessionalId());
                    message.setSender(register.getPatientId());
                    message.setTimestamp(LocalDateTime.now().minusMinutes(1));
                    message.setContent("Nuevo chat iniciado con éxito a las " + message.getTimestamp() + " entre paciente "
                            + message.getPatientId() + " y profesional " + message.getProfessionalId());
                    messageRepository.save(message);
                    temporalChatRepository.delete(register);
                    System.out.println("saliendo del if");
                }
            }
    }
}
