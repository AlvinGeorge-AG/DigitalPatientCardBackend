package com.DPC.DigitalPatientCardBackend.patient;

import java.util.UUID;

public class Disease {
    private String diseasename;
    private boolean status;
    private String date;
    private String diseaseid = UUID.randomUUID().toString();;
    public Disease() {
        // Required for Spring and Jackson
    }

    public Disease(String name,String date) {
        this.diseasename = name;
        this.status = false;
        this.date = date;
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
