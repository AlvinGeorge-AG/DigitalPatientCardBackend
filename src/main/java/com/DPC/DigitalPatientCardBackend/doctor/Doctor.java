package com.DPC.DigitalPatientCardBackend.doctor;


import com.DPC.DigitalPatientCardBackend.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String doctorid;
    private String certificate;
    private String specialization;
    private boolean status;
    public Doctor() {}
    public Doctor(String doctorid,String name, String username, int age, String address, String phoneNumber, String email, String password,String gender, String certificate, String specialization) {
        super(name, username, age, address, phoneNumber, email, password,gender);
        this.certificate = certificate;
        this.specialization = specialization;
        this.doctorid = doctorid;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getCertificate() { return certificate; }
    public void setCertificate(String certificate) {
        if(certificate != null && !certificate.isBlank()) {
            this.certificate = certificate;
        }
    }


    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) {
        if(specialization != null && !specialization.isBlank()) {
            this.specialization = specialization;
        }
    }


    public String getDoctorid() {
        return doctorid;
    }
    public void setDoctorid(String doctorid) {
        if(doctorid!=null &&  doctorid.length()==6 && !doctorid.isBlank()){
            this.doctorid = doctorid;
        }
    }

    public boolean isStatus() {
        return status;
    }
    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
