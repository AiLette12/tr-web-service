package com.example.tr.Mock.Controller;

import com.example.tr.Mock.DTO.MockOneResponse;
import com.example.tr.Mock.DTO.MockTwoResponse;
import com.example.tr.Mock.DTO.MockThreeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mock")
public class MockController {

    @GetMapping("/mock1")
    public ResponseEntity<MockOneResponse> getMock1() {
        MockOneResponse response = new MockOneResponse(
            "ORD-1234", 
            "SUCCESS", 
            "Pesanan diterima, stok warung dikurangi", 
            "Paket Ayam Geprek + Es Teh", 
            1
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mock2")
    public ResponseEntity<MockTwoResponse> getMock2() {
        MockTwoResponse response = new MockTwoResponse(
            "TRX-1232", 
            "SUCCESS", 
            "Saldo berhasil dipotong", 
            15000.0, 
            "E-Wallet Kampus"
        );
        return ResponseEntity.ok(response);
    }
// Code yang bener
    @GetMapping("/mock3")
    public ResponseEntity<MockThreeResponse> getMock3() {
        MockThreeResponse response = new MockThreeResponse(
            "DEL-1231", 
            "SUCCESS", 
            "Driver ditemukan, pesanan siap diantar", 
            "Pak Budi (Motor Merah)", 
            "10 Menit"
        );
        return ResponseEntity.ok(response);
    }

// Uji coba dibuat rusak
    // @GetMapping("/mock3")
    // public ResponseEntity<MockThreeResponse> getMock3() {
    //     // Sengaja dibuat error 500 untuk memicu Saga Rollback
    //     return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).build();
    // }

    /**
     * Rollback Mock 1: Membatalkan pesanan dan mengembalikan stok warung.
     */
    @PostMapping("/mock1/rollback")
    public ResponseEntity<Map<String, String>> rollbackMock1() {
        return ResponseEntity.ok(Map.of(
            "orderId", "ORD-1234",
            "status", "ROLLED_BACK",
            "message", "Pesanan dibatalkan, stok warung dikembalikan"
        ));
    }

    /**
     * Rollback Mock 2: Membatalkan pembayaran dan mengembalikan saldo.
     */
    @PostMapping("/mock2/rollback")
    public ResponseEntity<Map<String, String>> rollbackMock2() {
        return ResponseEntity.ok(Map.of(
            "transactionId", "TRX-1232",
            "status", "ROLLED_BACK",
            "message", "Saldo berhasil dikembalikan ke E-Wallet Kampus"
        ));
    }
}