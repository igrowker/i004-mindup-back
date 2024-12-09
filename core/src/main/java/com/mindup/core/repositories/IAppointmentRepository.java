package com.mindup.core.repositories;

import com.mindup.core.dtos.Appointment.ResponsePatientsDto;
import com.mindup.core.entities.AppointmentEntity;
import com.mindup.core.entities.User;
import com.mindup.core.enums.AppointmentStatus;

import feign.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IAppointmentRepository extends JpaRepository<AppointmentEntity, String> {

    Set<AppointmentEntity> getAppointmentsByPatient(User patient);

    Set<AppointmentEntity> getAppointmentsByPsychologist(User psychologist);

    @Query("SELECT a.patient FROM AppointmentEntity a " +
            "WHERE a.psychologist.userId = :psychologistId " +
            "AND a.softDelete IS NULL")
    Set<User> findDistinctPatientsByPsychologistId(@Param("psychologistId") String psychologistId);

    @Query("SELECT a FROM AppointmentEntity a " +
            "WHERE a.psychologist.userId = :psychologistId " +
            "AND a.patient.userId = :patientId " +
            "AND a.date >= CURRENT_TIMESTAMP " +
            "AND a.softDelete IS NULL " +
            "AND a.status != 'CANCELLED' " +
            "ORDER BY a.date ASC " +
            "LIMIT 1")
    Optional<AppointmentEntity> findNextAppointment(
            @Param("psychologistId") String psychologistId,
            @Param("patientId") String patientId);

    @Query("SELECT a FROM AppointmentEntity a " +
            "WHERE DATE(a.date) = :date " +
            "AND a.softDelete IS NULL")
    Optional<Set<AppointmentEntity>> findAppointmentEntitiesByDay(@Param("date") LocalDate date);

    Optional<AppointmentEntity> findByDate(LocalDate appointmentDate);

    // count patient appointments in a date range
    long countByPatientAndDateBetween(User patient, LocalDateTime start, LocalDateTime end);

    // count psycologist appointments in an date range
    long countByPsychologistAndDateBefore(User psychologist, LocalDateTime start);

    // Count patient appointments excluding the current appointment
    long countByPatientAndDateBetweenAndIdNot(User patient, LocalDateTime start, LocalDateTime end,
            String excludeAppointmentId);

    // Count psychologist appointments excluding the current appointment
    long countByPsychologistAndDateBetweenAndIdNot(User psychologist, LocalDateTime start, LocalDateTime end,
            String excludeAppointmentId);

    boolean existsByPsychologistAndDateBetween(
            User psychologist,
            LocalDateTime start,
            LocalDateTime end);

    boolean existsByPsychologistAndDateBetweenAndIdNot(User psychologist, LocalDateTime start, LocalDateTime end,
            String id);

    boolean existsByPsychologistAndDateBetweenAndStatusIn(
            User psychologist,
            LocalDateTime start,
            LocalDateTime end,
            List<AppointmentStatus> statuses);

    long countByPatientAndDateBetweenAndStatusNot(
            User patient,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay,
            AppointmentStatus status);

    boolean existsByPsychologistAndDateBetweenAndStatusInAndIdNot(
            User psychologist,
            LocalDateTime start,
            LocalDateTime end,
            List<AppointmentStatus> statuses,
            String excludedId);

            @Query("SELECT a FROM AppointmentEntity a " +
            "WHERE DATE(a.date) = :date " +
            "AND a.psychologist.userId = :psychologistId " +
            "AND a.softDelete IS NULL")
     Optional<Set<AppointmentEntity>> findAppointmentEntitiesByDayAndPsychologistId(@Param("date") LocalDate date,
                                                                                    @Param("psychologistId") String psychologistId);
}
