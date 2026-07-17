package com.example.tr.Mock.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockOneResponse {
    private String orderId;
    private String status;
    private String message;
    private String itemName;
    private int quantity;
}
// Arvid semua difolder mockup
