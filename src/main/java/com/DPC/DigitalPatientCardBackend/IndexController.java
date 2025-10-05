package com.DPC.DigitalPatientCardBackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "Welcome To Digital Patient Card Backend";
    }
}
