package com.DPC.DigitalPatientCardBackend.patient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "disease")
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String diseasename;

    @Column(nullable=false)
    private LocalDate date;

    private boolean status = false;

    @Column(name="verifiedDoctor")
    private String verifiedDoctor;

    private String specialization;

    // Add this field for the relationship
    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    private Patient patient;

    public Disease() {}

    public Disease(String diseasename, LocalDate date,String verifiedDoctor, String specialization) {
        this.diseasename = diseasename;
        this.date = date;
        this.verifiedDoctor = verifiedDoctor;
        this.specialization = specialization;
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiseasename() {
        return diseasename;
    }

    public void setDiseasename(String name) {
        if (name != null) {
            this.diseasename = name;
        }
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getVerifiedDoctor() {return verifiedDoctor;}

    public void setVerifiedDoctor(String verifiedDoctor) {this.verifiedDoctor = verifiedDoctor;}

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
