package com.DPC.DigitalPatientCardBackend.doctor;


import com.DPC.DigitalPatientCardBackend.user.User;


public class Doctor extends User {

    private String id;
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

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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
    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
