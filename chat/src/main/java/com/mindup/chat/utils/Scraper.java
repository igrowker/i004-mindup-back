package com.mindup.chat.utils;
import com.mindup.chat.dtos.ResponseEmergencyDto;
import com.mindup.chat.dtos.ResponseOtherResourcesDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Scraper {

    public List<ResponseEmergencyDto> getEmergencyContactList() throws IOException {
        File file = ResourceUtils.getFile("classpath:EmergenciasPsi.csv");
        List<ResponseEmergencyDto> emergencyContactList = new ArrayList<>();

        try (FileReader reader = new FileReader(file);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                String city = record.get("Localidad");
                String institution = record.get("Institución");
                String address = record.get("Dirección");
                String telephone = record.get("Teléfono");
                String sm = record.get("SM");
                String emergency = record.get("Guardia");
                ResponseEmergencyDto emergencyContact = new ResponseEmergencyDto(city, institution, address, telephone, sm, emergency);
                emergencyContactList.add(emergencyContact);
            }
        }

        return emergencyContactList;
    }
    public List<ResponseOtherResourcesDto> getOtherResourcesList() throws IOException {
        File file = ResourceUtils.getFile("classpath:OtrosNumeros.csv");
        List<ResponseOtherResourcesDto> otherResourcesList = new ArrayList<>();

        try (FileReader reader = new FileReader(file);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                String category = record.get("Categoría");
                String location = record.get("Ciudad y País");
                String institution = record.get("Institución");
                String telephones = record.get("Teléfonos");
                ResponseOtherResourcesDto otherResources = new ResponseOtherResourcesDto(category, location, institution, telephones);
                otherResourcesList.add(otherResources);
            }
        }

        return otherResourcesList;
    }
}