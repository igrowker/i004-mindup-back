package com.mindup.chat.services;

import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import com.mindup.chat.utils.Scraper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final Scraper scraper;

    @Override
    public List<ResponseEmergencyDto> getEmergencyContact() throws IOException {
        return scraper.getEmergencyContactList();
    }

    @Override
    public List<ResponseOtherResourcesDto> getOtherResources() throws IOException {
        return scraper.getOtherResourcesList();
    }
}
