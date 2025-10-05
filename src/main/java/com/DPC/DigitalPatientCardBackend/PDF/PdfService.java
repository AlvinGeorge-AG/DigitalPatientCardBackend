package com.DPC.DigitalPatientCardBackend.PDF;
import com.DPC.DigitalPatientCardBackend.PDF.PdfGenerator;
import com.DPC.DigitalPatientCardBackend.patient.Patient;
import com.DPC.DigitalPatientCardBackend.patient.Disease;
import com.DPC.DigitalPatientCardBackend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

    private final PatientRepository patientRepository;

    @Autowired
    public PdfService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public void createPatientPdf(String username, OutputStream out) throws Exception {
        // Fetch patient info from DB using username
        Patient patient = patientRepository.findByUsername(username);
        if (patient == null) {
            throw new RuntimeException("Patient not found: " + username);
        }

        // Build lines for PDF
        List<String> lines = List.of(
                "Patient Name: " + patient.getName(),
                "Patient Username: " + patient.getUsername(),
                "Phone Number: " + patient.getPhoneNumber(),
                "Blood Group: " + patient.getBloodgroup(),
                "Height: " + patient.getHeight() + " cm",
                "Weight: " + patient.getWeight() + " kg",
                "Patient ID :"+patient.getId()
        );

        // Build diseases table
        List<List<String>> table = new ArrayList<>();
        table.add(List.of("Disease", "Status"));
        for (Disease d : patient.getDiseases()) {
            table.add(List.of(d.getDiseasename(), d.isStatus() ? "Verified" : "Unverified"));
        }

        // Generate PDF
        PdfGenerator.generatePdf(out, "Digital Patient Card", lines, table);
    }
}
