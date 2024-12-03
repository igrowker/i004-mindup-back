package com.mindup.core.services;

import com.mindup.core.dtos.User.PatientPreferencesDTO;
import com.mindup.core.entities.User;
import com.mindup.core.enums.Gender;
import com.mindup.core.exceptions.InvalidPreferencesException;
import com.mindup.core.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final UserRepository userRepository;

    public PatientService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validatePreferences(PatientPreferencesDTO preferencesDTO) {
        if (preferencesDTO.getIsBelow35() == null) {
            throw new InvalidPreferencesException("Age preference cannot be null.");
        }
        if (preferencesDTO.getGender() == null) {
            throw new InvalidPreferencesException("Gender preference cannot be null.");
        }
    }

    public List<User> searchPsychologists(PatientPreferencesDTO preferencesDTO) {
        Gender gender = preferencesDTO.getGender();
        Boolean isBelow35 = preferencesDTO.getIsBelow35();

        List<User> psychologists = userRepository.findPsychologistsByGender(gender);

        return psychologists.stream()
                .filter(p -> (isBelow35 == null
                || (isBelow35 && p.getAge() < 35)
                || (!isBelow35 && p.getAge() >= 35)))
                .collect(Collectors.toList());
    }
}
