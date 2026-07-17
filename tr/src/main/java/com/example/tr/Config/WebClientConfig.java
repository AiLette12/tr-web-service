package com.example.tr.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Configuration;
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}

// TUGAS ARVID
// menyiapkan "bahan percobaan". 
// Anggota 1 harus membuat 3 endpoint tiruan (/mock1, /mock2, /mock3) 
// yang mengembalikan data JSON sederhana. 
// Selain itu, dia harus men-setup konfigurasi WebClient (alat/library HTTP client) 
// yang akan digunakan oleh anggota lain untuk menembak endpoint tersebut.
