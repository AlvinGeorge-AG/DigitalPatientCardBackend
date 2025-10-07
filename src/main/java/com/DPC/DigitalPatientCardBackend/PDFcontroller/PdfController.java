package com.DPC.DigitalPatientCardBackend.PDFcontroller;

import com.DPC.DigitalPatientCardBackend.PDF.PdfService;
import com.DPC.DigitalPatientCardBackend.doctor.Doctor;
import com.DPC.DigitalPatientCardBackend.patient.Patient;
import com.DPC.DigitalPatientCardBackend.repository.DoctorRepository;
import com.DPC.DigitalPatientCardBackend.repository.PatientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class PdfController {

    private final PdfService pdfService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PdfController(PdfService pdfService, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.pdfService = pdfService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    // Allow your React dev server
    @GetMapping("/download/patient-pdf")
    public void downloadPatientPdf(@RequestParam String username, HttpServletResponse response, HttpSession session) {
        Object sessionUserObj = session.getAttribute("username");
        if (sessionUserObj == null) {
            response.setStatus(401); // Unauthorized
            return;
        }
        String sessionUser = sessionUserObj.toString();

        Patient patient = patientRepository.findByUsername(username);
        Doctor doctor = doctorRepository.findByUsername(sessionUser);

        boolean isPatient = patient != null && sessionUser.equals(patient.getUsername());
        boolean isDoctor = doctor != null;

        if (isPatient || isDoctor) {
            try {
                response.setContentType(MediaType.APPLICATION_PDF_VALUE);
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=patient.pdf");
                pdfService.createPatientPdf(username, response.getOutputStream());
            } catch (Exception e) {
                response.setStatus(500);
            }
        } else {
            response.setStatus(403); // Forbidden
        }
    }

}
