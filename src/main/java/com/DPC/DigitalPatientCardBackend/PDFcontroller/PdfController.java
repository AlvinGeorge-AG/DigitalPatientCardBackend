package com.DPC.DigitalPatientCardBackend.PDFcontroller;

import com.DPC.DigitalPatientCardBackend.PDF.PdfService;
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

    public PdfController(PdfService pdfService, PatientRepository patientRepository) {
        this.pdfService = pdfService;
        this.patientRepository = patientRepository;
    }

    // Allow your React dev server
    @GetMapping("/download/patient-pdf")
    public void downloadPatientPdf(@RequestParam String username, HttpServletResponse response, HttpSession session) {
        if(session.getAttribute("username")!=null && session.getAttribute("username").equals(patientRepository.findByUsername(username).getUsername())){
            try {
                response.setContentType(MediaType.APPLICATION_PDF_VALUE);
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=patient.pdf");
                pdfService.createPatientPdf(username, response.getOutputStream());
            } catch (Exception e) {
                response.setStatus(500);
            }
        }
    }
}
