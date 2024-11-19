package com.mindup.core.repositories;

import com.mindup.core.entities.AppointmentEntity;
import com.mindup.core.entities.User;
import com.mindup.core.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IAppointmentRepository extends JpaRepository<AppointmentEntity, String> {
    // #################################################################################/*
    // by the moment it will return an entity instead of dto, this going to be changed soon!!!/*
    // #################################################################################/*
    Set<AppointmentEntity> getAppointmentsByPatient (User patient);
    Set<AppointmentEntity> getAppointmentsByPsychologist (User psychologist);
    Optional<AppointmentEntity> findByDate(LocalDate appointmentDate);
}
