package com.mindup.core.services;

import com.mindup.core.dtos.User.PatientPreferencesDTO;
import com.mindup.core.entities.User;
import com.mindup.core.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private UserRepository userRepository;

    public List<User> searchPsychologists(PatientPreferencesDTO preferencesDTO) {
        return userRepository.findPsychologistsByPreferences(
                preferencesDTO.getIsBelow35(),
                preferencesDTO.getGender());
    }
}
