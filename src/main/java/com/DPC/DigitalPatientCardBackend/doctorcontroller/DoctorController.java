package com.DPC.DigitalPatientCardBackend.doctorcontroller;


import com.DPC.DigitalPatientCardBackend.doctor.Doctor;
import com.DPC.DigitalPatientCardBackend.doctor.Referral;
import com.DPC.DigitalPatientCardBackend.repository.DoctorRepository;
import com.DPC.DigitalPatientCardBackend.patient.Disease;
import com.DPC.DigitalPatientCardBackend.patient.Patient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.DPC.DigitalPatientCardBackend.repository.PatientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    private final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    public DoctorController(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;

    }

    @GetMapping("/register")
    public ResponseEntity<?> register(HttpSession session){
        if(session.getAttribute("username")==null){
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Login");
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(HttpSession session, @RequestBody Doctor doctor){
        if(session.getAttribute("username")==null && doctorRepository.findByUsername(doctor.getUsername())==null){
            session.setAttribute("username",doctor.getUsername());
            String password = passwordEncoder.encode(doctor.getPassword());
            doctor.setPassword(password);
            doctorRepository.save(doctor);
            return   ResponseEntity.ok("Doctor Registered successfully!");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor Already Logged In Please Logout First!") ;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(HttpSession session, @RequestParam String username, @RequestParam String password){
        if(session.getAttribute("username")==null){
            Doctor doctor = doctorRepository.findByUsername(username);
            if(doctor!=null && passwordEncoder.matches(password,doctor.getPassword())){
                session.setAttribute("username",doctor.getUsername());
                return ResponseEntity.ok("Logged in successfully!");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid username or password");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Doctor Already Logged in");
    }


    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(HttpSession session){
        if(session.getAttribute("username")==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Login");
        }
        String username = (String) session.getAttribute("username");
        Doctor doctor = doctorRepository.findByUsername(username);
        return ResponseEntity.ok(doctor);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session){
        if(session.getAttribute("username")!=null){
            session.invalidate();
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Login");
    }


    //Doctor Specific Controllers
    @GetMapping("/search")
    public ResponseEntity<?> search(HttpSession session, @RequestParam String patientusername){
        if(session.getAttribute("username")!=null){
            Patient patient = patientRepository.findByUsername(patientusername);
            if(patient!=null){
//                LocalDateTime now = LocalDateTime.now();
//                DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                return ResponseEntity.ok(patient);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient Not Found");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Login");
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verify(HttpSession session,@RequestParam Long patientId,@RequestParam Long diseaseid){
        if(session.getAttribute("username")!=null){
            Patient patient = patientRepository.findPatientById(patientId);
            if(patient!=null){
                List<Disease> list = patient.getDiseases();
                for(Disease d : list){
                    if(d.getId().equals(diseaseid)){
                        if(d.isStatus()){
                            d.setStatus(false);
                            patientRepository.save(patient);
                            return ResponseEntity.ok(Map.of(
                                    "message", "Status updated successfully",
                                    "diseaseId", diseaseid,
                                    "newStatus", d.isStatus()
                            ));
                        }
                        d.setStatus(true);
                        patientRepository.save(patient);
                        return ResponseEntity.ok(Map.of(
                                "message", "Status updated successfully",
                                "diseaseId", diseaseid,
                                "newStatus", d.isStatus(),
                                "disease name" ,d.getDiseasename()
                        ));
                    }
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Disease Not Found");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient Not Found");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Login");
    }


    @PostMapping("/refer")
    public ResponseEntity<?> refer(HttpSession session,
                        @RequestParam String patientusername,
                        @RequestParam String referredDoctorUsername,
                        @RequestParam(required = false, defaultValue = "") String remarks) {
        if (session.getAttribute("username") != null) {
            Doctor referringDoctor = doctorRepository.findByUsername((String) session.getAttribute("username"));
            Doctor _referredDoctor = doctorRepository.findByUsername(referredDoctorUsername);
            Patient patient = patientRepository.findByUsername(patientusername);

            if (referringDoctor != null && _referredDoctor != null && patient != null) {
                // create and attach referral on referring doctor
                _referredDoctor.referPatient(patient.getUsername(), referringDoctor.getUsername(), remarks);
                doctorRepository.save(_referredDoctor);
                return ResponseEntity.ok().body(Map.of(
                        "DoctorReferred","Success",
                        "ReferringDoctor", referringDoctor.getUsername(),
                        "Referred Doctor",_referredDoctor.getUsername()
                ));
            }
            if(_referredDoctor == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ReferredDoctor Not Found");
            if(patient == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient Not Found");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Login");
    }

    @GetMapping("/getreferrals")
    public ResponseEntity<?> getReferals(HttpSession session){
        if(session.getAttribute("username")!=null){
            Doctor thisDoctor = doctorRepository.findByUsername((String) session.getAttribute("username"));
            List<Referral> referrals = thisDoctor.getReferrals();
            return ResponseEntity.ok(referrals);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Login");
    }

}
