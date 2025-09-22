package com.DPC.DigitalPatientCardBackend.admin;

import com.DPC.DigitalPatientCardBackend.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {
    @Id
    private Long id;
    private String adminpin;

    public Admin(String adminpin) {
        this.adminpin = adminpin;
    }

    public Admin() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdminpin() {
        return adminpin;
    }
}

