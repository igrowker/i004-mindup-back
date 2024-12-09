package com.mindup.core.services;

import com.mindup.core.dtos.Appointment.*;

import java.util.Set;


public interface IAppointmentService {
    
    Set<ResponseAppointmentDto> getPatientReservedAppointments(String patientId);
    Set<ResponseAppointmentDto> getPshychologistReservedAppointment(String psychologistId);
    Set<ResponseAppointmentDto> getPshychologistPendientAppointment(String psychologistId);
    Set<ResponseAppointmentDto> getAppointmentsByPatient(String patientId);
    Set<ResponseAppointmentDto> getAppointmentsByPsychologist(String psychologistId);
    Set<ResponsePatientsDto> getPsychologistPatients(String id);
    Set<ResponseAppointmentDateDto> getAppointmentsByDay(RequestAppointmentsByDayDto requestAppointmentsByDayDto);
    Set<ResponseAppointmentDto> getAppointmentsPending();
    Set<ResponseAppointmentDto> getAppointmentsAccepted();
    Set<ResponseAppointmentDto> getAppointmetsCanceled();
    
    ResponseCreateAppointmentDto add(RequestCreateAppointmentDto requestDto);
    ResponseAppointmentDto aceptAppointment(String id);
    ResponseAppointmentDto cancelAppointment(String id);
    ResponseAppointmentDto update(RequestUpdateAppointmentDto requestDto);

    ResponseDeleteAppointmentDto delete(String appointmentId);
    ResponseReactivateAppointmentDto reactivateAppointment(String appointmentId);

}