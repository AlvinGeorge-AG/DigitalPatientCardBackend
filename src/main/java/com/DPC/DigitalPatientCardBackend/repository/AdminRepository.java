package com.DPC.DigitalPatientCardBackend.repository;

import com.DPC.DigitalPatientCardBackend.admin.Admin;
import com.DPC.DigitalPatientCardBackend.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUsername(String username);
}
