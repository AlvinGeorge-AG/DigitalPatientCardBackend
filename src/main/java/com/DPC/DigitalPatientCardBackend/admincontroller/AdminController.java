package com.DPC.DigitalPatientCardBackend.admincontroller;

import com.DPC.DigitalPatientCardBackend.admin.Admin;
import com.DPC.DigitalPatientCardBackend.doctor.Doctor;
import com.DPC.DigitalPatientCardBackend.patient.Patient;
import com.DPC.DigitalPatientCardBackend.repository.AdminRepository;
import com.DPC.DigitalPatientCardBackend.repository.DoctorRepository;
import com.DPC.DigitalPatientCardBackend.repository.PatientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final DoctorRepository doctorRepository;
    private final AdminRepository adminRepository;
    private final PatientRepository patientRepository;

    public AdminController(DoctorRepository doctorRepository, AdminRepository adminRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.adminRepository = adminRepository;
        this.patientRepository = patientRepository;
    }

    private final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    // ===== Admin Login =====
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username , @RequestParam String password,@RequestParam String adminpin ){
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setAdminpin(passwordEncoder.encode(adminpin));
        adminRepository.save(admin);
        return ResponseEntity.ok().body("Success Admin");
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginverify(@RequestParam String username,
                                         @RequestParam String password,
                                         @RequestParam String adminpin,
                                         HttpSession session) {
        if (session.getAttribute("username") == null) {
            Admin admin = adminRepository.findByUsername(username);
            if (admin!=null && passwordEncoder.matches(password,admin.getPassword()) && passwordEncoder.matches(adminpin,admin.getAdminpin()) ) {
                session.setAttribute("username", username);
                session.setAttribute("adminpin", adminpin);

                return ResponseEntity.ok().body("Login successful");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        return ResponseEntity.ok("Already logged in");
    }

    // ===== Dashboard Data =====
    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(HttpSession session) {
        if (session.getAttribute("username") != null) {
            List<Patient> patients = patientRepository.findAll();
            List<Doctor> doctors = doctorRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("patients", patients);
            response.put("doctors", doctors);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
    }

    // ===== Delete Patient =====
    @Transactional
    @DeleteMapping("/patient/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
        try {
            patientRepository.delete(patient); // Cascade deletes diseases
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting patient");
        }
        return ResponseEntity.ok("Patient and all associated diseases deleted successfully");
    }

    // ===== Delete Doctor =====
    @Transactional
    @DeleteMapping("/doctor/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        if (!doctorRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        try {
            doctorRepository.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting doctor");
        }
        return ResponseEntity.ok("Doctor deleted successfully");
    }


    // ===== Verify Doctor =====
    @PostMapping("/verify/doctor/{id}")
    public ResponseEntity<?> verifyDoctor(@PathVariable Long id) {
        Doctor doctor = doctorRepository.findDoctorById(id);
        if (doctor != null) {
            doctor.setStatus(true);
            doctorRepository.save(doctor);
            return ResponseEntity.ok("Doctor verified successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
    }

    // ===== Logout =====
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }
    @PostMapping("/patients/all")
    public ResponseEntity<?> patientsall(HttpSession session) {
        if(session.getAttribute("username") != null) {
            List<Patient> patients = patientRepository.findAll();
            return ResponseEntity.ok(patients);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");

    }
    @PostMapping("/doctors/all")
    public ResponseEntity<?> doctorsall(HttpSession session) {
        if(session.getAttribute("username") != null) {
            List<Doctor> doctors = doctorRepository.findAll();
            return ResponseEntity.ok(doctors);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");

    }
}