package com.DPC.DigitalPatientCardBackend.patient;

import com.DPC.DigitalPatientCardBackend.user.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float height;
    private float weight;
    private String bloodgroup;
    private String bloodpressure;
    private String sugar;
    private boolean smoking;
    @ElementCollection
    private List<String> allergies = new ArrayList<>();

    @ElementCollection
    private List<String> pastconditions = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "patients_diseases",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "disease_id")
    )
    private List<Disease> diseases = new ArrayList<>();

    //constructors
    public Patient() {}

    public Patient(String name, String username, int age, String address, String phoneNumber, String email, String password,String gender) {
        super(name, username, age, address, phoneNumber, email, password,gender);
    }


    public void addDisease(Disease disease) {
        diseases.add(disease);
    }
    public void removeDisease(Disease disease) {
        diseases.remove(disease);

    }

    // Patient-specific getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public float getHeight() { return height; }
    public void setHeight(float height) {
        if(height>30) {
            this.height = height;
        }
    }


    public float getWeight() { return weight; }
    public void setWeight(float weight) {
        if(weight>3) {
            this.weight = weight;
        }
    }


    public String getBloodgroup() { return bloodgroup; }
    public void setBloodgroup(String bloodgroup) {
        if(bloodgroup!=null && !bloodgroup.isEmpty() && bloodgroup.length()<3) {
            this.bloodgroup = bloodgroup;
        }
    }


    public boolean isSmoking() {
        return smoking;
    }
    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }
    public String getSugar() { return sugar; }
    public void setSugar(String sugar) {
        if(!sugar.isEmpty() && sugar.length()<10) {
            this.sugar = sugar;
        }
    }

    public String getBloodpressure() { return bloodpressure; }
    public void setBloodpressure(String bloodpressure) {
        if(!bloodpressure.isBlank() && bloodpressure.length() < 7) {
            this.bloodpressure = bloodpressure;
        }
    }


    public List<String> getAllergies() {
        return allergies;
    }
    public void setAllergies(String allergies) {
        if(allergies!=null && !allergies.isEmpty()) {
            this.allergies.add(allergies);
        }

    }


    public List<String> getPastconditions() {
        return pastconditions;
    }
    public void setPastconditions(String pastconditions) {
        if(pastconditions!=null && !pastconditions.isEmpty()) {

            this.pastconditions.add(pastconditions);
        }
    }


    public List<Disease> getDiseases() {
        return diseases;
    }
    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }
}


