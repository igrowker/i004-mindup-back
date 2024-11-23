package com.mindup.chat.services.implementations;

import com.mindup.chat.entities.AvailablePsychologists;
import com.mindup.chat.repositories.AvailablePsychologistsRepository;
import com.mindup.chat.services.AvailablePsychologistsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AvailablePsychologistsServiceImpl implements AvailablePsychologistsService {

    private final AvailablePsychologistsRepository availablePsychologistsRepository;

    @Override
    public boolean subscribeProfessional(String id) {
        AvailablePsychologists professional = new AvailablePsychologists();
                professional.setProfessionalId(id);
                professional.setTimestamp(LocalDateTime.now());
        availablePsychologistsRepository.save(professional);
        return true;
    }

    @Override
    public String findFirstProfessional() {
        var availablePsychologist = availablePsychologistsRepository.findAll().get(0);
        String id =availablePsychologist.getProfessionalId();
        availablePsychologistsRepository.delete(availablePsychologist);
        return id;
    }
}
