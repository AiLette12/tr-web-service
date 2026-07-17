package com.example.tr.Mock.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MockTwoResponse {
    private String transactionId;
    private String status;
    private String message;
    private double amountDeducted;
    private String paymentMethod;
}
