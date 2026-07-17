package com.example.tr.Service;

import com.example.tr.Mock.DTO.MockOneResponse;
import com.example.tr.Mock.DTO.MockTwoResponse;
import com.example.tr.Mock.DTO.MockThreeResponse;
import com.example.tr.Model.SharedDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class OrchestratorService {
    private static final Logger log = LoggerFactory.getLogger(OrchestratorService.class);
    private final WebClient webClient;

    public OrchestratorService(
            WebClient.Builder webClientBuilder,
            @Value("${orchestrator.mock-base-url:http://localhost:8080}") String mockBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(mockBaseUrl).build();
    }

    /**
     * Menjalankan seluruh langkah orchestration dengan pola Saga.
     * Jika salah satu langkah gagal, langkah-langkah sebelumnya yang sudah
     * berhasil akan di-rollback (compensating transaction) secara terbalik
     * untuk menjaga konsistensi data.
     */
    public SharedDto executeStep1And2() {
        MockOneResponse mock1Response = null;
        MockTwoResponse mock2Response = null;
        MockThreeResponse mock3Response = null;

        // ==================== STEP 1: Pemesanan ====================
        try {
            mock1Response = Objects.requireNonNull(
                    webClient.get()
                            .uri("/mock/mock1")
                            .retrieve()
                            .bodyToMono(MockOneResponse.class)
                            .block(),
                    "Respons dari mock1 tidak boleh kosong");
            log.info("Step 1: Berhasil memanggil mock 1 - Pesanan {} diterima", mock1Response.getOrderId());
        } catch (Exception e) {
            log.error("Step 1 GAGAL: Tidak dapat membuat pesanan - {}", e.getMessage());
            // Tidak ada langkah sebelumnya, tidak perlu rollback
            throw new RuntimeException("Orchestration gagal di Step 1: " + e.getMessage(), e);
        }

        // ==================== STEP 2: Pembayaran ====================
        try {
            mock2Response = Objects.requireNonNull(
                    webClient.get()
                            .uri("/mock/mock2")
                            .retrieve()
                            .bodyToMono(MockTwoResponse.class)
                            .block(),
                    "Respons dari mock2 tidak boleh kosong");
            log.info("Step 2: Berhasil memanggil mock 2 - Transaksi {} saldo dipotong Rp{}",
                    mock2Response.getTransactionId(), mock2Response.getAmountDeducted());
        } catch (Exception e) {
            log.error("Step 2 GAGAL: Pembayaran gagal diproses - {}", e.getMessage());
            log.info("Menjalankan rollback untuk Mock 1 (Pesanan)...");
            compensateMock1(mock1Response);
            throw new RuntimeException("Orchestration gagal di Step 2. Rollback Step 1 selesai.", e);
        }

        // ==================== STEP 3: Pengiriman ====================
        try {
            mock3Response = Objects.requireNonNull(
                    webClient.get()
                            .uri("/mock/mock3")
                            .retrieve()
                            .bodyToMono(MockThreeResponse.class)
                            .block(),
                    "Respons dari mock3 tidak boleh kosong");
            log.info("Step 3: Berhasil memanggil mock 3 - Driver {} ditemukan, ETA {}",
                    mock3Response.getDriverName(), mock3Response.getEstimatedTime());
        } catch (Exception e) {
            log.error("Step 3 GAGAL: Pencarian driver gagal - {}", e.getMessage());
            log.info("Menjalankan rollback untuk Mock 1 dan 2...");
            compensateMock2(mock2Response);
            compensateMock1(mock1Response);
            throw new RuntimeException("Orchestration gagal di Step 3. Rollback Step 1 & 2 selesai.", e);
        }

        log.info("Semua langkah orchestration berhasil!");
        return new SharedDto(mock1Response, mock2Response, mock3Response);
    }

    // =====================================================
    // COMPENSATING TRANSACTIONS (Saga Rollback)
    // =====================================================

    /**
     * Kompensasi Mock 1: Membatalkan pesanan dan mengembalikan stok warung.
     */
    private void compensateMock1(MockOneResponse originalResponse) {
        try {
            webClient.post()
                    .uri("/mock/mock1/rollback")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Rollback Step 1 BERHASIL: Pesanan {} dibatalkan, stok dikembalikan",
                    originalResponse != null ? originalResponse.getOrderId() : "UNKNOWN");
        } catch (Exception rollbackEx) {
            log.error("Rollback Step 1 GAGAL: Tidak dapat membatalkan pesanan - {}. PERLU INTERVENSI MANUAL!",
                    rollbackEx.getMessage());
        }
    }

    /**
     * Kompensasi Mock 2: Membatalkan pembayaran dan mengembalikan saldo.
     */
    private void compensateMock2(MockTwoResponse originalResponse) {
        try {
            webClient.post()
                    .uri("/mock/mock2/rollback")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Rollback Step 2 BERHASIL: Transaksi {} dibatalkan, saldo Rp{} dikembalikan ke {}",
                    originalResponse != null ? originalResponse.getTransactionId() : "UNKNOWN",
                    originalResponse != null ? originalResponse.getAmountDeducted() : "?",
                    originalResponse != null ? originalResponse.getPaymentMethod() : "?");
        } catch (Exception rollbackEx) {
            log.error("Rollback Step 2 GAGAL: Tidak dapat mengembalikan saldo - {}. PERLU INTERVENSI MANUAL!",
                    rollbackEx.getMessage());
        }
    }
}

