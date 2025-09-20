package com.DPC.DigitalPatientCardBackend.repository;


import com.DPC.DigitalPatientCardBackend.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByUsername(String username);
    Patient findPatientById(long id);
    // You get save(), findAll(), findById(), deleteById(), etc. for free
}
