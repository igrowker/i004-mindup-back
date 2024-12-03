package com.mindup.core.dtos.User;

import com.mindup.core.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatientPreferencesDTO {

    @NotNull(message = "Age preference is required")
    private Boolean isBelow35;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;
}
