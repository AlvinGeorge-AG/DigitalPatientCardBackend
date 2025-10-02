package com.DPC.DigitalPatientCardBackend.PDFcontroller;

import com.DPC.DigitalPatientCardBackend.PDF.PdfService;
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

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    // Allow your React dev server
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/download/patient-pdf")
    public void downloadPatientPdf(@RequestParam String username, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=patient.pdf");
            pdfService.createPatientPdf(username, response.getOutputStream());
        } catch (Exception e) {
            response.setStatus(500);
        }
    }
}
