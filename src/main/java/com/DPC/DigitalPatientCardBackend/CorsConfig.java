package com.DPC.DigitalPatientCardBackend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        String frontendUrl = System.getenv("https://dpcfrontend.vercel.app/");
        if (frontendUrl == null || frontendUrl.isBlank()) {
            // Default to localhost dev URL if env variable is missing
            frontendUrl = "http://localhost:5173";
        }

        String finalFrontendUrl = frontendUrl; // needed for lambda
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(finalFrontendUrl)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
