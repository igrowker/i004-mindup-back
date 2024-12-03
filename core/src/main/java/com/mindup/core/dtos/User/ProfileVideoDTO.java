package com.mindup.core.dtos.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileVideoDTO {

    @NotBlank(message = "Video URL cannot be blank")
    private String video;
}
