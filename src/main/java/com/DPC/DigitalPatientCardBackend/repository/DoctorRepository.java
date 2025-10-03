package com.DPC.DigitalPatientCardBackend.repository;

import com.DPC.DigitalPatientCardBackend.doctor.Doctor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Doctor findByUsername(String username);
    Doctor findDoctorById(Long id);
    void deleteById(@NotNull Long id);
}
