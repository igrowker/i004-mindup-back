package com.mindup.core.services;

import com.mindup.core.entities.AppointmentEntity;
import com.mindup.core.repositories.IAppointmentRepository;

import java.net.http.HttpHeaders;
import java.util.Set;

public interface IAppointmentService{
    Set<AppointmentEntity> getReservedAppointments(HttpHeaders httpHeaders);
    AppointmentEntity add (HttpHeaders httpHeaders, AppointmentEntity appointmentEntity);
    AppointmentEntity update (HttpHeaders httpHeaders, AppointmentEntity appointmentEntity);
    AppointmentEntity delete (HttpHeaders httpHeaders, AppointmentEntity appointmentEntity);
}
