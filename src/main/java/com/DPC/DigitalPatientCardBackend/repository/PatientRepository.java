package com.DPC.DigitalPatientCardBackend.repository;

import com.DPC.DigitalPatientCardBackend.patient.Patient;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByUsername(String username);
    void deleteById(@NotNull Long id);
    Patient findPatientById(Long id);
}

