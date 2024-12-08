package com.mindup.core.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mindup.core.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(schema="mindupdbschema", name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String preferences;

    private String chosenPsychologist;

    @Column
    private String profile;

    private Boolean availability = false;

    @Column
    private String image;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @Transient
    private int age;

    @Column
    private String location;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Column
    private String phone;

    @Column
    private String tuition;

    @Column
    private String specialty;

    @Column
    private String information;

    @Column
    private String video;

    public int getAge() {
        if (birth != null) {
            return Period.between(birth, LocalDate.now()).getYears();
        }
        return 0;
    }

    public void setSpecialty(String specialty) {
        if (this.role == Role.PSYCHOLOGIST) {
            this.specialty = specialty;
        } else {
            throw new IllegalArgumentException("Only users with role PSYCHOLOGIST can set specialty.");
        }
    }

    public void setTuition(String tuition) {
        if (this.role == Role.PSYCHOLOGIST) {
            this.tuition = tuition;
        } else {
            throw new IllegalArgumentException("Only users with role PSYCHOLOGIST can set tuition.");
        }
    }

    public void setVideo(String video) {
        if (this.role == Role.PSYCHOLOGIST) {
            this.video = video;
        } else {
            throw new IllegalArgumentException("Only users with role PSYCHOLOGIST can set a video.");
        }
    }
}
