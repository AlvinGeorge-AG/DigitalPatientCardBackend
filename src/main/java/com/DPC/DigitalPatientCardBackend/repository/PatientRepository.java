package com.DPC.DigitalPatientCardBackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.DPC.DigitalPatientCardBackend.patient.Patient;

import java.util.List;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    Patient findByEmail(String email);
    Patient findByUsername(String username);
    Patient findPatientById(String id);
    Patient deletePatientByUsername(String username);
    List<Patient> findAll();
}
