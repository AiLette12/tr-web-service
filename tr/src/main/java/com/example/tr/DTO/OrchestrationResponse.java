package com.example.tr.DTO;

import com.example.tr.Mock.DTO.MockOneResponse;
import com.example.tr.Mock.DTO.MockThreeResponse;
import com.example.tr.Mock.DTO.MockTwoResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrchestrationResponse {
    private String status;   // "SUCCESS" atau "FAILED"
    private String message; 
    private MockOneResponse order;      // hasil step 1
    private MockTwoResponse payment;    // hasil step 2
    private MockThreeResponse delivery; // hasil step 3
}