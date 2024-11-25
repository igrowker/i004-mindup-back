package com.mindup.chat.services.implementations;

import com.mindup.chat.dtos.TemporalChatIdDto;
import com.mindup.chat.entities.AvailablePsychologists;
import com.mindup.chat.repositories.AvailablePsychologistsRepository;
import com.mindup.chat.repositories.TemporalChatRepository;
import com.mindup.chat.services.AvailablePsychologistsService;
import com.mindup.chat.utils.AvailabilityScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AvailablePsychologistsServiceImpl implements AvailablePsychologistsService {

    private final AvailablePsychologistsRepository availablePsychologistsRepository;
    private final AvailabilityScheduler availabilityScheduler;

    @Override
    public boolean subscribeProfessional(String id) throws IOException {
        AvailablePsychologists professional = new AvailablePsychologists();
                professional.setProfessionalId(id);
                professional.setTimestamp(LocalDateTime.now());
        availablePsychologistsRepository.save(professional);
        availabilityScheduler.checkAndTurnProfessionalUnavailable(true);
        return true;
    }

    @Override
    public TemporalChatIdDto findFirstProfessional() {
        var availablePsychologist = availablePsychologistsRepository.findAll().get(0);
        String professionalId =availablePsychologist.getProfessionalId();


        return null;
    }
}
