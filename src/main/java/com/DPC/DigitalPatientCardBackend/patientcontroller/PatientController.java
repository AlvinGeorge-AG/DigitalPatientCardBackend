package com.DPC.DigitalPatientCardBackend.patientcontroller;

import com.DPC.DigitalPatientCardBackend.patient.Disease;
import com.DPC.DigitalPatientCardBackend.repository.PatientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.DPC.DigitalPatientCardBackend.patient.Patient;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(HttpSession session){
        if(session.getAttribute("username")==null){
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient Already Logged In Please Logout First!") ;
    }

    @GetMapping("/register")
    public ResponseEntity<?> register(HttpSession session){
        if(session.getAttribute("username")==null){
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient Already Logged In Please Logout First!") ;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerpatient(@RequestBody Patient patient,HttpSession session){
        String username=patient.getUsername();
        if(patientRepository.findByUsername(username)==null && session.getAttribute("username")==null){
            session.setAttribute("username",patient.getUsername());
            String password = passwordEncoder.encode(patient.getPassword());
            patient.setPassword(password);
            patientRepository.save(patient);
            return ResponseEntity.ok("Patient registered successfully");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient already exists");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginverify(@RequestParam String username, @RequestParam String password, HttpSession session){
        if(session.getAttribute("username")==null) {
            Patient patient = patientRepository.findByUsername(username);
            if(patient==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("patient not found");
            }
            if (username.equals(patient.getUsername()) && passwordEncoder.matches(password, patient.getPassword())) {
                session.setAttribute("username", patient.getUsername());
                return ResponseEntity.ok().body("Login Successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password/Patient Don't Exist!");
            }
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient Already Logged in!");
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dash(HttpSession session){
        if(session.getAttribute("username")==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please Login");
        }
        String username = (String) session.getAttribute("username");
        Patient patient = patientRepository.findByUsername(username);
        return ResponseEntity.ok(patient);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        if(session.getAttribute("username")!=null){
            session.invalidate();
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Login");
    }

    @GetMapping("/diseases")
    public ResponseEntity<?> getDiseases(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please login");
        }
        Patient patient = patientRepository.findByUsername(username);
        return ResponseEntity.ok(patient.getDiseases());
    }

    //Patient Specific Controllers
    @PostMapping("/adddisease")
    public ResponseEntity<?> addDisease(@RequestParam String description,@RequestParam String specialization, HttpSession session) {

        if (session.getAttribute("username") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first");
        }


        if (description.isBlank() || description.length() < 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid description");
        }


        Patient patient = patientRepository.findByUsername(session.getAttribute("username").toString());
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }


        LocalDate now = LocalDate.now();
        Disease disease = new Disease(description, now,"",specialization);
        disease.setPatient(patient); // Set owning side first
        patient.getDiseases().add(disease); // Add to collection


        //  Save patient (disease will be persisted because of CascadeType.PERSIST)
        patientRepository.save(patient);

        return ResponseEntity.ok("Disease added successfully");
    }



    @GetMapping("/update-profile")
    public ResponseEntity<String> updateProfile(HttpSession session){
        if(session.getAttribute("username")==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please login");
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Patient patient, HttpSession session){
        if(session.getAttribute("username")==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please login");
        }
        Patient existingPatient = patientRepository.findByUsername(session.getAttribute("username").toString());
        existingPatient.setGender(patient.getGender());
        existingPatient.setHeight(patient.getHeight());
        existingPatient.setWeight(patient.getWeight());
        existingPatient.setBloodgroup(patient.getBloodgroup());
        existingPatient.setBloodpressure(patient.getBloodpressure());
        existingPatient.setSugar(patient.getSugar());
        existingPatient.setSmoking(patient.isSmoking());
        existingPatient.setAge(patient.getAge());
//        existingPatient.setAllergies(patient.getAllergies());
//        existingPatient.setPastconditions(patient.getPastconditions());
        patientRepository.save(existingPatient);
        return ResponseEntity.ok("Patient updated successfully");
    }


    @DeleteMapping("/delete-disease/{id}")
    public ResponseEntity<?> deleteDisease(HttpSession session, @PathVariable Long id) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first");
        }
        Patient patient = patientRepository.findByUsername(username);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
        Disease toRemove = null;
        for (Disease d : patient.getDiseases()) {
            if (d.getId().equals(id)) {
                toRemove = d;
                break;
            }
        }
        if (toRemove == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Disease not found for this patient");
        }
        patient.getDiseases().remove(toRemove); // Remove from patient's list
        toRemove.setPatient(null); // Break owning side (JPA best practice)
        patientRepository.save(patient); // OrphanRemoval will delete it from DB
        return ResponseEntity.ok("Disease deleted successfully");
    }
















    @GetMapping("/dpc")
    public ResponseEntity<?> dpc(HttpSession session){
        String username = (String) session.getAttribute("username");
        if(username==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please login");
        }
        Patient patient = patientRepository.findByUsername(username);
        return ResponseEntity.ok(patient);
    }


}
