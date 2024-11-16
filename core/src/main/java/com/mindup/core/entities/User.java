package com.mindup.core.entities;

import com.mindup.core.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column
    private String preferences;
    @Column
    private String profile;

    @OneToMany(targetEntity = AppointmentEntity.class,fetch = FetchType.EAGER, mappedBy = "patient")
    private List<AppointmentEntity> patientAppointments;

    @OneToMany(targetEntity = AppointmentEntity.class,fetch = FetchType.EAGER, mappedBy = "psychologist")
    private List<AppointmentEntity> psychologistAppointments;
}
