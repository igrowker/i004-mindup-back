package com.mindup.core.services.IMPL;

import com.mindup.core.dtos.Appointment.*;
import com.mindup.core.entities.AppointmentEntity;
import com.mindup.core.entities.User;
import com.mindup.core.enums.AppointmentStatus;
import com.mindup.core.enums.Role;
import com.mindup.core.mappers.AppointmentMapper;
import com.mindup.core.repositories.IAppointmentRepository;
import com.mindup.core.repositories.UserRepository;
import com.mindup.core.services.IAppointmentService;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpHeaders;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {
    private IAppointmentRepository appointmentRepository;
    private AppointmentMapper appointmentMapper;
    private UserRepository userRepository;

    // changes incoming soon/*
    @Override
    public Set<ResponseAppointmentDto> getPatientReservedAppointments(Long id) {
        // Checking if patient exists
        User patient = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        
        // Checking Role/*
        if(patient.getRole() != Role.PATIENT) throw new IllegalArgumentException("User must be a patient");
        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();
        Set<AppointmentEntity> acceptedList = appointmentEntityList.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.ACCEPTED && appointmentEntity.getPatient().getUserId() == patient.getUserId())
                .collect(Collectors.toSet());
        return appointmentMapper.toResponseDtoSet(acceptedList);
    }
    // #######################################################################/*

    // changes incoming soon/*
    @Override   
    public Set<ResponseAppointmentDto> getPshychologistReservedAppointment(Long id) {
        // Checking if psychologist exists
        User psychologist = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Psychologist not found"));

        // Checking Role/*
        if(psychologist.getRole() != Role.PSYCHOLOGIST) throw new IllegalArgumentException("User must be a psychologist");
        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();
        Set<AppointmentEntity> acceptedList = appointmentEntityList.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.ACCEPTED && appointmentEntity.getPsychologist().getUserId() == psychologist.getUserId())
                .collect(Collectors.toSet());
                return appointmentMapper.toResponseDtoSet(acceptedList);
            }
    // #######################################################################/*

    @Override
    public ResponseAppointmentDto add(RequestCreateAppointmentDto requestDto) {
        // Checking if patient and psychologist exists
        User patient = userRepository.findById(requestDto.patientId()).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        User psychologist = userRepository.findById(requestDto.psychologistId()).orElseThrow(() -> new IllegalArgumentException("Psychologist not found"));
    
        // Checking Roles/*
        if(patient.getRole() != Role.PATIENT) throw new IllegalArgumentException("User must be a patient to schedule an appointment");
        if(psychologist.getRole() != Role.PSYCHOLOGIST) throw new IllegalArgumentException("User must be a psychologist to schedule an appointment");

        // scheduling an appointment/*
        AppointmentEntity appointmen = AppointmentEntity.builder()
                .patient(patient)
                .psychologist(psychologist)
                .date(requestDto.date())
                .status(AppointmentStatus.PENDING)
                .build();

                AppointmentEntity savedAppointment = appointmentRepository.save(appointmen);
        return appointmentMapper.toResponseDto(savedAppointment);
    }

    @Override
    public ResponseAppointmentDto update(RequestUpdateAppointmentDto requestUpdateAppointmentDto) {
        // Checking if patient and psychologist exists
        User patient = userRepository.findById(requestUpdateAppointmentDto.patientId()).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        User psychologist = userRepository.findById(requestUpdateAppointmentDto.psychologistId()).orElseThrow(() -> new IllegalArgumentException("Psychologist not found"));

        // Checking Roles/*
        if(patient.getRole() != Role.PATIENT) throw new IllegalArgumentException("User must be a patient to schedule an appointment");
        if(psychologist.getRole() != Role.PSYCHOLOGIST) throw new IllegalArgumentException("User must be a psychologist to schedule an appointment");
        // Checking if appointment exist/*
        Optional<AppointmentEntity> entityOptional = appointmentRepository.findById(requestUpdateAppointmentDto.appointmenId());
        if(!entityOptional.isPresent()) throw new IllegalArgumentException("appointment not found");
        // getting appointment and updating /*

        AppointmentEntity updatedEntity = entityOptional.get();
        updatedEntity.setPatient(patient);
        updatedEntity.setPsychologist(psychologist);
        updatedEntity.setDate(requestUpdateAppointmentDto.date());

        AppointmentEntity updatedAppointment = appointmentRepository.save(updatedEntity);
        return appointmentMapper.toResponseDto(updatedAppointment);
    }

    @Override
    public Set<ResponseAppointmentDto> getAppointmentsByPatient(Long id) {
        // Checking if patient exists
        User patient = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        if(patient.getRole() != Role.PATIENT) throw new IllegalArgumentException("Bad argument, user isn't a patient");

        Set<AppointmentEntity> appointments = appointmentRepository.getAppointmentsByPatient(patient);
        return appointmentMapper.toResponseDtoSet(appointments);
    }

    @Override
    public Set<ResponseAppointmentDto> getAppointmentsByPsychologist(Long id) {
        // Checking if psycologist exists
        User psychologist = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        if(psychologist.getRole() != Role.PSYCHOLOGIST) throw new IllegalArgumentException("Bad argument, user isn't a psychologist");

        Set<AppointmentEntity> appointments = appointmentRepository.getAppointmentsByPsychologist(psychologist);
        return appointmentMapper.toResponseDtoSet(appointments);
    }

    @Override
    public void delete(Long appointmentId) {
        // Checking if appointment exist/*
        if(!appointmentRepository.existsById(appointmentId)) throw new IllegalArgumentException("appointment doesn't exist");
        appointmentRepository.deleteById(appointmentId);

    }
}
