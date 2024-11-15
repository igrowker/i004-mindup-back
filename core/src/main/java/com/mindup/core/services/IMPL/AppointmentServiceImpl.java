package com.mindup.core.services.IMPL;

import com.mindup.core.entities.AppointmentEntity;
import com.mindup.core.entities.User;
import com.mindup.core.enums.AppointmentStatus;
import com.mindup.core.enums.Role;
import com.mindup.core.repositories.IAppointmentRepository;
import com.mindup.core.repositories.UserRepository;
import com.mindup.core.services.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.http.HttpHeaders;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AppointmentServiceImpl implements IAppointmentService {
    private IAppointmentRepository appointmentRepository;
    // changes incoming soon/*
    @Override
    public Set<AppointmentEntity> getPatientReservedAppointments(User patient) {
        // Checking Role/*
        if(patient.getRole() != Role.PATIENT) throw new IllegalArgumentException("User must be a patient");
        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();
        Set<AppointmentEntity> acceptedList = appointmentEntityList.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.ACCEPTED && appointmentEntity.getPatient().getUserId() == patient.getUserId())
                .collect(Collectors.toSet());
        return acceptedList;
    }
    // #######################################################################/*

    // changes incoming soon/*
    @Override
    public Set<AppointmentEntity> getPshychologistReservedAppointment(User psychologist) {
        // Checking Role/*
        if(psychologist.getRole() != Role.PSYCHOLOGIST) throw new IllegalArgumentException("User must be a psychologist");
        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();
        Set<AppointmentEntity> acceptedList = appointmentEntityList.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.ACCEPTED && appointmentEntity.getPsychologist().getUserId() == psychologist.getUserId())
                .collect(Collectors.toSet());
        return acceptedList;
    }
    // #######################################################################/*

    @Override
    public AppointmentEntity add(User patient, User psychologist, LocalDateTime date) {
        // Checking Roles/*
        if(patient.getRole() != Role.PATIENT) throw new IllegalArgumentException("User must be a patient to schedule an appointment");
        if(psychologist.getRole() != Role.PSYCHOLOGIST) throw new IllegalArgumentException("User must be a psychologist to schedule an appointment");
        // scheduling an appointment/*
        AppointmentEntity appointmen = AppointmentEntity.builder()
                .patient(patient)
                .psychologist(psychologist)
                .date(date)
                .status(AppointmentStatus.PENDING)
                .build();

        return appointmentRepository.save(appointmen);
    }

    @Override
    public AppointmentEntity update(Long appointmentId, User patient, User psychologist, LocalDateTime date) {
        // Checking Roles/*
        if(patient.getRole() != Role.PATIENT) throw new IllegalArgumentException("User must be a patient to schedule an appointment");
        if(psychologist.getRole() != Role.PSYCHOLOGIST) throw new IllegalArgumentException("User must be a psychologist to schedule an appointment");
        // Checking if appointment exist/*
        Optional<AppointmentEntity> entityOptional = appointmentRepository.findById(appointmentId);
        if(!entityOptional.isPresent()) throw new IllegalArgumentException("appointment not found");
        // getting appointment and updating /*
        AppointmentEntity updatedEntity = entityOptional.get();
        updatedEntity.setPatient(patient);
        updatedEntity.setPsychologist(psychologist);
        updatedEntity.setDate(date);

        return updatedEntity;
    }

    @Override
    public Set<AppointmentEntity> getAppointmentsByPatient(User patient) {
        if(patient.getRole() != Role.PATIENT) throw new IllegalArgumentException("Bad argument, user isn't a patient");
        return appointmentRepository.getAppointmentsByPatient(patient);
    }

    @Override
    public Set<AppointmentEntity> getAppointmentsByPsychologist(User psychologist) {
        if(psychologist.getRole() != Role.PSYCHOLOGIST) throw new IllegalArgumentException("Bad argument, user isn't a psychologist");
        return appointmentRepository.getAppointmentsByPsychologist(psychologist);
    }

    @Override
    public void delete(Long appointmentId) {
        // Checking if appointment exist/*
        if(!appointmentRepository.existsById(appointmentId)) throw new IllegalArgumentException("appointment doesn't exist");
        appointmentRepository.deleteById(appointmentId);

    }
}
