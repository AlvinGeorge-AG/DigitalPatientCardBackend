package com.DPC.DigitalPatientCardBackend.patient;

import com.DPC.DigitalPatientCardBackend.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "patients")
public class Patient extends User {
    @Id
    private String id;
    private float height;
    private float weight;
    private String bloodgroup;
    private String bloodpressure;
    private String sugar;
    private boolean smoking;
    private List<String> allergies = new ArrayList<>();
    private List<String> pastconditions = new ArrayList<>();
    private List<Disease> diseases = new ArrayList<>();

    public Patient() {}

    public Patient(String name, String username, int age, String address, String phoneNumber, String email, String password,String gender) {
        super(name, username, age, address, phoneNumber, email, password,gender);
    }

    // Patient-specific getters/setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }



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
        if(!bloodgroup.isEmpty() && bloodgroup.length()<3) {
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
        if(sugar!=null && !sugar.isEmpty() && sugar.length()<10) {
            this.sugar = sugar;
        }
    }

    public String getBloodpressure() { return bloodpressure; }
    public void setBloodpressure(String bloodpressure) {
        if(bloodpressure!=null && !bloodpressure.isEmpty() && bloodpressure.length()<7) {
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


