package com.DPC.DigitalPatientCardBackend.doctor;

import jakarta.persistence.*;

@Entity
@Table(name = "referral")
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String patientUsername;
    private String referringDoctor;
    private String referredDoctor;
    private String reason;

    public Referral(String patientUsername, String referringDoctor, String referredDoctor, String reason) {
        this.patientUsername = patientUsername;
        this.referringDoctor = referringDoctor;
        this.referredDoctor = referredDoctor;
        this.reason = reason;
    }

    public Referral() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getters & Setters
    public String getPatientUsername() { return patientUsername; }
    public void setPatientUsername(String patientUsername) { this.patientUsername = patientUsername; }

    public String getReferringDoctor() { return referringDoctor; }
    public void setReferringDoctor(String referringDoctor) { this.referringDoctor = referringDoctor; }

    public String getReferredDoctor() { return referredDoctor; }
    public void setReferredDoctor(String referredDoctor) { this.referredDoctor = referredDoctor; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}