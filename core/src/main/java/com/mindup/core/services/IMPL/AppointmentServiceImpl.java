package com.mindup.core.services.IMPL;

import com.mindup.core.entities.AppointmentEntity;
import com.mindup.core.enums.AppointmentStatus;
import com.mindup.core.repositories.IAppointmentRepository;
import com.mindup.core.services.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AppointmentServiceImpl implements IAppointmentService {
    private IAppointmentRepository appointmentRepository;
    @Override
    public Set<AppointmentEntity> getReservedAppointments(HttpHeaders httpHeaders){
        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();
         Set<AppointmentEntity> appointmentsAccepted = appointmentEntityList.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.ACCEPTED)
                .collect(Collectors.toSet());
         return appointmentsAccepted;
    }

    @Override
    public AppointmentEntity add(HttpHeaders httpHeaders, AppointmentEntity appointmentEntity) {
        return null;
    }

    @Override
    public AppointmentEntity update(HttpHeaders httpHeaders, AppointmentEntity appointmentEntity) {
        return null;
    }

    @Override
    public AppointmentEntity delete(HttpHeaders httpHeaders, AppointmentEntity appointmentEntity) {
        return null;
    }
}
