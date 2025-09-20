package com.DPC.DigitalPatientCardBackend.repository;


import com.DPC.DigitalPatientCardBackend.doctor.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DoctorRepository extends MongoRepository<Doctor,String> {
    Doctor findByUsername(String username);
    Doctor findDoctorById(String id);
    Doctor deleteDoctorByUsername(String username);
    List<Doctor> findAll();
}
