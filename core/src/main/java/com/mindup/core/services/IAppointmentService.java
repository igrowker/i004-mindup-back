package com.mindup.core.services;

import com.mindup.core.entities.AppointmentEntity;
import com.mindup.core.entities.User;
import com.mindup.core.repositories.IAppointmentRepository;

import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface IAppointmentService{
    Set<AppointmentEntity> getPatientReservedAppointments(User patient);
    Set<AppointmentEntity> getPshychologistReservedAppointment(User psychologist);
    AppointmentEntity add (User patient, User psychologist, LocalDateTime date);
    AppointmentEntity update (Long appointmentId, User patient, User psychologist, LocalDateTime date);
    Set<AppointmentEntity> getAppointmentsByPatient (User patient);
    Set<AppointmentEntity> getAppointmentsByPsychologist (User psychologist);
    void delete (Long appointmentId);
}
