package com.DPC.DigitalPatientCardBackend.PDF;

import com.DPC.DigitalPatientCardBackend.PDF.PdfGenerator;
import com.DPC.DigitalPatientCardBackend.patient.Patient;
import com.DPC.DigitalPatientCardBackend.patient.Disease;
import com.DPC.DigitalPatientCardBackend.repository.PatientRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PdfService {

    public static class PageBorder extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            cb.setColorStroke(new BaseColor(0, 121, 184)); // blue, feel free to change
            cb.setLineWidth(3f);
            float left = document.left() - 8;
            float right = document.right() + 8;
            float top = document.top() + 8;
            float bottom = document.bottom() - 8;
            cb.rectangle(left, bottom, right - left, top - bottom);
            cb.stroke();
            cb.restoreState();
        }
    }

    private final PatientRepository patientRepository;

    @Autowired
    public PdfService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public void createPatientPdf(String username, OutputStream out) throws Exception {
        Patient patient = patientRepository.findByUsername(username);
        if (patient == null) {
            throw new RuntimeException("Patient not found: " + username);
        }

        // Format last updated timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        List<String> lines = getStrings(patient, formattedNow);

        List<List<String>> table = new ArrayList<>();
        table.add(List.of("Disease", "Status", "Verified By"));
        for (Disease d : patient.getDiseases()) {
            table.add(List.of(d.getDiseasename(), d.isStatus() ? "Verified" : "Unverified", !Objects.equals(d.getVerifiedDoctor(), "") ? "Dr. " + d.getVerifiedDoctor():""));
        }

        PdfGenerator.generatePdf(out, "Digital Patient Card", lines, table);
    }

    private static @NotNull List<String> getStrings(Patient patient, String formattedNow) {
        List<String> lines = new ArrayList<>();
        lines.add("Patient Name: " + patient.getName());
        lines.add("Patient Username: " + patient.getUsername());
        //lines.add("Phone Number: " + patient.getPhoneNumber());
        lines.add("Blood Group: " + patient.getBloodgroup());
        lines.add("Height: " + patient.getHeight() + " cm");
        lines.add("Weight: " + patient.getWeight() + " kg");
        lines.add("Patient ID : POO" + patient.getId());
        lines.add(" "); // space
        lines.add("Last Updated: " + formattedNow);
        return lines;
    }
}
