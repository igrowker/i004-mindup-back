package com.mindup.core.entities;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "email_verifications")
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String verificationToken;

    @Column(nullable = false)
    private boolean verified;

}
