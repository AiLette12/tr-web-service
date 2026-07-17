package com.example.tr.Mock.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockThreeResponse {
    private String deliveryId;
    private String status;
    private String message;
    private String driverName;
    private String estimatedTime;
}
