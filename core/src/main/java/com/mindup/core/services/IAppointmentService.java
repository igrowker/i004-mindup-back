package com.mindup.core.services;

import com.mindup.core.dtos.Appointment.*;

import java.util.Set;


public interface IAppointmentService {
    Set<ResponseAppointmentDto> getPatientReservedAppointments(Long patientId);
    Set<ResponseAppointmentDto> getPshychologistReservedAppointment(Long psychologistId);
    ResponseAppointmentDto add(RequestCreateAppointmentDto requestDto);
    ResponseAppointmentDto update(RequestUpdateAppointmentDto requestDto);
    Set<ResponseAppointmentDto> getAppointmentsByPatient(Long patientId);
    Set<ResponseAppointmentDto> getAppointmentsByPsychologist(Long psychologistId);
    void delete(Long appointmentId);
}