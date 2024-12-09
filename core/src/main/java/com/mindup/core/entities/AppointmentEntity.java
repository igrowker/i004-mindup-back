package com.mindup.core.entities;

import com.mindup.core.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Table(schema="mindupdbschema", name = "appointments")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id",nullable = false)
    private User patient;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "psychologist_id",nullable = false)
    private User psychologist;

    @Column(name = "appointment_date",nullable = false)
    private LocalDateTime date;

    private LocalDateTime softDelete;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;
}
