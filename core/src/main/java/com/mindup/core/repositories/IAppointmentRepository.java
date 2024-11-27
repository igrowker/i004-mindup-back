package com.mindup.core.repositories;

import com.mindup.core.entities.AppointmentEntity;
import com.mindup.core.entities.User;
import com.mindup.core.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IAppointmentRepository extends JpaRepository<AppointmentEntity, String> {

    Set<AppointmentEntity> getAppointmentsByPatient (User patient);
    Set<AppointmentEntity> getAppointmentsByPsychologist (User psychologist);
    Optional<AppointmentEntity> findByDate(LocalDate appointmentDate);

    // count patient appointments in a date range
    long countByPatientAndDateBetween(User patient, LocalDateTime start, LocalDateTime end);
    
    // count psycologist appointments in an date range
    long countByPsychologistAndDateBetween(User psychologist, LocalDateTime start, LocalDateTime end);

    // Count patient appointments excluding the current appointment
    long countByPatientAndDateBetweenAndIdNot(User patient, LocalDateTime start, LocalDateTime end, String excludeAppointmentId);
        
    // Count psychologist appointments excluding the current appointment
    long countByPsychologistAndDateBetweenAndIdNot(User psychologist, LocalDateTime start, LocalDateTime end, String excludeAppointmentId);
}
