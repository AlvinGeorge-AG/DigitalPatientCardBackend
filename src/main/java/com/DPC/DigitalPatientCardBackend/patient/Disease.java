package com.DPC.DigitalPatientCardBackend.patient;

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

    @Column(unique=true, nullable=false, updatable=false)
    private String diseaseid;


    public Disease() {}

    public Disease(String diseasename, LocalDate date) {
        this.diseasename = diseasename;
        this.date = date;
        this.diseaseid = UUID.randomUUID().toString();
    }


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

    public String getDiseaseid() {
        return diseaseid;
    }
    public void setDiseaseid(String diseaseid) {
        this.diseaseid = diseaseid;
    }

}
