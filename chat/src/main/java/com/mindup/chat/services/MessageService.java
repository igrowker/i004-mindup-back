package com.mindup.chat.services;

import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;

import java.io.IOException;
import java.util.List;

public interface MessageService {

List<ResponseEmergencyDto> getEmergencyContact() throws IOException;
List<ResponseOtherResourcesDto> getOtherResources() throws IOException;
}
