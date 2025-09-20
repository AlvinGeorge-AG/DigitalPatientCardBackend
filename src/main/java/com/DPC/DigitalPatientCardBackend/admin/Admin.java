package com.DPC.DigitalPatientCardBackend.admin;

import com.DPC.DigitalPatientCardBackend.user.User;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="admin")
public class Admin extends User {
    private String adminpin;

    public Admin(String adminpin) {
        this.adminpin = adminpin;
    }

    public String getAdminpin() {
        return adminpin;
    }
}

