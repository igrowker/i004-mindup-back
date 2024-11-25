package com.mindup.chat.services;

import com.mindup.chat.dtos.TemporalChatIdDto;

import java.io.IOException;

public interface AvailablePsychologistsService {

boolean subscribeProfessional(String id) throws IOException;

TemporalChatIdDto findFirstProfessional();
}
