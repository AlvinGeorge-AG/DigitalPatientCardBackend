package com.DPC.DigitalPatientCardBackend.patient;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Disease {
    @Id
    private Long id;
    private String diseasename;
    private boolean status;
    private String date;
    private String diseaseid = UUID.randomUUID().toString();
    public Disease() {
        // Required for Spring and Jackson
    }

    public Disease(String name,String date) {
        this.diseasename = name;
        this.status = false;
        this.date = date;
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

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDiseaseid() {
        return diseaseid;
    }
    public void setDiseaseid() {}
}
