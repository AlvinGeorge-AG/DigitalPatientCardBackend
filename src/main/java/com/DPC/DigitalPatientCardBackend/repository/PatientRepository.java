package com.DPC.DigitalPatientCardBackend.repository;

import com.DPC.DigitalPatientCardBackend.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByUsername(String username);
    void deletePatientByUsername(String username);
    Patient findPatientById(Long id);

    // Add this for PDF service
    Patient findByPhoneNumber(String phoneNumber);
}
