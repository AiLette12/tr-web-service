package com.example.tr;

import com.example.tr.Service.OrchestratorService;
import com.example.tr.Model.SharedDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TrApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrApplication.class, args);
    }

    // Testing
    @Bean
    CommandLineRunner testRunner(OrchestratorService orchestratorService) {
        return args -> {
            System.out.println("\n========== MULAI TEST ORCHESTRATOR ==========");
            try {
                // Manggil
                SharedDto result = orchestratorService.executeStep1And2();
                // Nampilin hasil
                System.out.println("Data Mock 1: " + result.getMock1Response().getItemName());
                System.out.println("Data Mock 2: " + result.getMock2Response().getAmountDeducted());
                System.out.println("Data Mock 3: " + result.getMock3Response().getDriverName());
                System.out.println("========== TEST BERHASIL ==========\n");
            } catch (Exception e) {
                System.err.println("Terjadi Error: " + e.getMessage());
            }
        };
    }
}