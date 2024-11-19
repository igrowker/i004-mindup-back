package com.mindup.core.services;

import com.mindup.core.dtos.Appointment.*;

import java.util.Set;


public interface IAppointmentService {
    Set<ResponseAppointmentDto> getPatientReservedAppointments(String patientId);
    Set<ResponseAppointmentDto> getPshychologistReservedAppointment(String psychologistId); //Esa h. :P
    ResponseAppointmentDto add(RequestCreateAppointmentDto requestDto);
    ResponseAppointmentDto update(RequestUpdateAppointmentDto requestDto);
    Set<ResponseAppointmentDto> getAppointmentsByPatient(String patientId);
    Set<ResponseAppointmentDto> getAppointmentsByPsychologist(String psychologistId);
    void delete(String appointmentId);
}