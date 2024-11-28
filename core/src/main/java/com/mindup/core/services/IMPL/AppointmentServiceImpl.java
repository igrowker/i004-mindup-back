package com.mindup.core.services.IMPL;

import com.mindup.core.dtos.Appointment.*;
import com.mindup.core.entities.AppointmentEntity;
import com.mindup.core.entities.User;
import com.mindup.core.enums.AppointmentStatus;
import com.mindup.core.enums.Role;
import com.mindup.core.exceptions.AppointmentConflictException;
import com.mindup.core.exceptions.EmptyAppointmentException;
import com.mindup.core.exceptions.EmptyAppointmentsByStateException;
import com.mindup.core.exceptions.ResourceAlreadyExistsException;
import com.mindup.core.exceptions.ResourceNotFoundException;
import com.mindup.core.exceptions.RoleMismatchException;
import com.mindup.core.exceptions.UserNotFoundException;
import com.mindup.core.mappers.AppointmentMapper;
import com.mindup.core.repositories.IAppointmentRepository;
import com.mindup.core.repositories.UserRepository;
import com.mindup.core.services.IAppointmentService;
import com.mindup.core.validations.UserValidation;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {
    private final IAppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;
    private final UserValidation userValidation;

    @Override
    public Set<ResponseAppointmentDto> getPatientReservedAppointments(String id) {
        // Cheking if patient exists
        User patient = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Patient not found"));

        // Checking patient role
        if (patient.getRole() != Role.PATIENT)
            throw new IllegalArgumentException("User must be a patient");

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();
        Set<AppointmentEntity> acceptedList = appointmentEntityList.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.ACCEPTED &&
                        appointmentEntity.getPatient().getUserId() == patient.getUserId() &&
                        appointmentEntity.getSoftDelete() == null)
                .collect(Collectors.toSet());

        if (acceptedList.isEmpty()) {
            throw new EmptyAppointmentException("User appointments not found");
        }
        return appointmentMapper.toResponseDtoSet(acceptedList);
    }

    @Override
    public Set<ResponseAppointmentDto> getPshychologistReservedAppointment(String id) {
        // Cheking if patient exists
        User psychologist = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Psychologist not found"));

        // Checking psychologist role
        if (psychologist.getRole() != Role.PSYCHOLOGIST)
            throw new RoleMismatchException("User must be a psychologist");

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();
        Set<AppointmentEntity> acceptedList = appointmentEntityList.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.ACCEPTED &&
                        appointmentEntity.getPsychologist().getUserId() == psychologist.getUserId() &&
                        appointmentEntity.getSoftDelete() == null)
                .collect(Collectors.toSet());

        if (acceptedList.isEmpty()) {
            throw new EmptyAppointmentException("User appointments not found");
        }
        return appointmentMapper.toResponseDtoSet(acceptedList);
    }
    // #######################################################################/*

    @Override
    public Set<ResponseAppointmentDto> getAppointmentsByPatient(String id) {
        // Checking if patient exists
        User patient = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Patient not found"));

        if (patient.getRole() != Role.PATIENT)
            throw new RoleMismatchException("Bad argument, user isn't a patient");

        Set<AppointmentEntity> appointments = appointmentRepository.getAppointmentsByPatient(patient);

        if (appointments.isEmpty()) {
            throw new EmptyAppointmentException("User appointments not found");
        }
        return appointmentMapper.toResponseDtoSet(appointments);
    }

    @Override
    public Set<ResponseAppointmentDto> getAppointmentsByPsychologist(String id) {
        // Checking if psycologist exists
        User psychologist = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));

        if (psychologist.getRole() != Role.PSYCHOLOGIST)
            throw new RoleMismatchException("Bad argument, user isn't a psychologist");

        Set<AppointmentEntity> appointments = appointmentRepository.getAppointmentsByPsychologist(psychologist);

        if (appointments.isEmpty()) {
            throw new EmptyAppointmentException("User appointments not found");
        }
        return appointmentMapper.toResponseDtoSet(appointments);
    }

    @Override
    public Set<ResponseAppointmentDto> getAppointmentsPending() {
        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();
        Set<AppointmentEntity> pendingList = appointmentEntityList.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.PENDING &&
                        appointmentEntity.getSoftDelete() == null)
                .collect(Collectors.toSet());

        if (pendingList.isEmpty()) {
            throw new EmptyAppointmentsByStateException("This appointment status doesnt have any appointment");
        }
        return appointmentMapper.toResponseDtoSet(pendingList);
    }

    @Override
    public Set<ResponseAppointmentDto> getAppointmentsAccepted() {
        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();
        Set<AppointmentEntity> acceptedList = appointmentEntityList.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.ACCEPTED &&
                        appointmentEntity.getSoftDelete() == null)
                .collect(Collectors.toSet());

        if (acceptedList.isEmpty()) {
            throw new EmptyAppointmentsByStateException("This appointment status doesnt have any appointment");
        }
        return appointmentMapper.toResponseDtoSet(acceptedList);
    }

    @Override
    public Set<ResponseAppointmentDto> getAppointmetsCanceled() {
        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();
        Set<AppointmentEntity> canceledList = appointmentEntityList.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.CANCELED &&
                        appointmentEntity.getSoftDelete() == null)
                .collect(Collectors.toSet());

        if (canceledList.isEmpty()) {
            throw new EmptyAppointmentsByStateException("This appointment status doesnt have any appointment");
        }
        return appointmentMapper.toResponseDtoSet(canceledList);
    }

    @Override
    public ResponseCreateAppointmentDto add(RequestCreateAppointmentDto requestDto) {
        // Checking if patient and psychologist exists
        User patient = userRepository.findById(requestDto.patientId())
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));
        User psychologist = userRepository.findById(requestDto.psychologistId())
                .orElseThrow(() -> new UserNotFoundException("Psychologist not found"));

        // Checking Roles
        if (patient.getRole() != Role.PATIENT)
            throw new RoleMismatchException("User must be a patient to schedule an appointment");
        if (psychologist.getRole() != Role.PSYCHOLOGIST)
            throw new RoleMismatchException("User must be a psychologist to schedule an appointment");

        // Check patient's appointments on the same day
        LocalDateTime startOfDay = requestDto.date().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long patientAppointmentsCount = appointmentRepository.countByPatientAndDateBetween(
                patient, startOfDay, endOfDay);

        if (patientAppointmentsCount > 0) {
            throw new AppointmentConflictException("Patient already has an appointment on this day");
        }

        // Check for overlapping
        LocalDateTime appointmentStart = requestDto.date();
        LocalDateTime bufferBefore = appointmentStart.minusMinutes(30);
        LocalDateTime bufferAfter = appointmentStart.plusMinutes(30);

        // Check if psychologist has any conflicting appointments
        boolean hasConflictingAppointments = appointmentRepository.existsByPsychologistAndDateBetween(
                psychologist, bufferBefore, bufferAfter);

        if (hasConflictingAppointments) {
            throw new AppointmentConflictException("Psychologist has conflicting appointments nearby");
        }

        // Scheduling an appointment
        AppointmentEntity appointment = AppointmentEntity.builder()
                .patient(patient)
                .psychologist(psychologist)
                .date(requestDto.date())
                .status(AppointmentStatus.PENDING)
                .build();

        AppointmentEntity savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.appointmentToResponseCreateAppointmentDto(savedAppointment);
    }

    @Override
    public ResponseAppointmentDto aceptAppointment(String id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment doesn't exist"));

        if (appointment.getStatus() == AppointmentStatus.ACCEPTED) {
            throw new ResourceAlreadyExistsException("This appointment is already ACEPTED");
        }

        appointment.setStatus(AppointmentStatus.ACCEPTED);
        appointmentRepository.save(appointment);

        return appointmentMapper.toResponseDto(appointment);
    }

    @Override
    public ResponseAppointmentDto cancelAppointment(String id) {

        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment doesn't exist"));

        if (appointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new ResourceAlreadyExistsException("This appointment is already CANCELED");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(appointment);

        return appointmentMapper.toResponseDto(appointment);
    };

    //
    @Override
    public ResponseAppointmentDto update(RequestUpdateAppointmentDto requestUpdateAppointmentDto) {
        // checking if appointment exists
        AppointmentEntity existingAppointment = appointmentRepository
                .findById(requestUpdateAppointmentDto.appointmenId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment doesn't exist"));

        // Find the patient and psychologist for the existing appointment
        User patient = existingAppointment.getPatient();
        User psychologist = existingAppointment.getPsychologist();

        // Check patient appointments on the new date
        LocalDateTime startOfDay = requestUpdateAppointmentDto.date().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long patientAppointmentsCount = appointmentRepository.countByPatientAndDateBetween(
                patient, startOfDay, endOfDay);

        if (patientAppointmentsCount > 0) {
            throw new AppointmentConflictException("Patient already has an appointment on this day");
        }

        // Check for overlapping
        LocalDateTime appointmentStart = requestUpdateAppointmentDto.date();
        LocalDateTime bufferBefore = appointmentStart.minusMinutes(30);
        LocalDateTime bufferAfter = appointmentStart.plusMinutes(30);

        // Check if psychologist has any conflicting appointments
        boolean hasConflictingAppointments = appointmentRepository.existsByPsychologistAndDateBetween(
                psychologist, bufferBefore, bufferAfter);

        if (hasConflictingAppointments) {
            throw new AppointmentConflictException("Psychologist has conflicting appointments nearby");
        }

        // Update appointment details
        existingAppointment.setDate(requestUpdateAppointmentDto.date());
        AppointmentEntity updatedAppointment = appointmentRepository.save(existingAppointment);

        return appointmentMapper.toResponseDto(updatedAppointment);
    }

    @Override
    public ResponseDeleteAppointmentDto delete(String id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment doesn't exist"));

        ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");
        appointment.setSoftDelete(ZonedDateTime.now(zoneId).toLocalDateTime());

        appointment.setStatus(AppointmentStatus.CANCELED);

        AppointmentEntity deletedAppointment = appointmentRepository.save(appointment);

        return ResponseDeleteAppointmentDto.builder()
                .appointmentId(deletedAppointment.getId())
                .dateSoftDelete(deletedAppointment.getSoftDelete())
                .status(deletedAppointment.getStatus())
                .build();
    }

    @Override
    public ResponseReactivateAppointmentDto reactivateAppointment(String id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment doesn't exist"));

        appointment.setSoftDelete(null);
        appointment.setStatus(AppointmentStatus.PENDING);
        AppointmentEntity reactivatedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.appointmentToResponseReactivateAppointmentDto(reactivatedAppointment);
    }

}