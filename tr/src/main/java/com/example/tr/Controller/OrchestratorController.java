package com.example.tr.Controller;

import com.example.tr.DTO.OrchestrationResponse;
import com.example.tr.Model.SharedDto;
import com.example.tr.Service.OrchestratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint utama yang dipanggil client untuk menjalankan seluruh
 * proses orchestration (Step 1: Pemesanan -> Step 2: Pembayaran ->
 * Step 3: Pengiriman) dan mengembalikan hasil gabungannya dalam
 * satu response JSON.
 *
 * Tugas Anggota 5 - Agregasi & Dokumen.
 */
@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController {

    private final OrchestratorService orchestratorService;

    public OrchestratorController(OrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrchestrationResponse> checkout() {
        try {
            SharedDto result = orchestratorService.executeStep1And2();

            OrchestrationResponse response = new OrchestrationResponse(
                    "SUCCESS",
                    "Pesanan berhasil diproses dari pemesanan sampai pengiriman",
                    result.getMock1Response(),
                    result.getMock2Response(),
                    result.getMock3Response()
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            OrchestrationResponse response = new OrchestrationResponse(
                    "FAILED",
                    e.getMessage(),
                    null, null, null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
// TUGAS YEFTA
// Mengambil semua hasil data yang sukses dari Anggota 2 dan 3, 
// lalu membungkusnya ke dalam satu Data Transfer Object (DTO) 
// untuk di-return sebagai satu JSON utuh ke client. 
// Setelah kode selesai digabung, dia bertugas membuat dokumentasi di file README.md 
// dan menyertakan screenshot terminal/Postman.