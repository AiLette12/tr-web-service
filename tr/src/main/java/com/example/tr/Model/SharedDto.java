package com.example.tr.Model;

import com.example.tr.Mock.DTO.MockOneResponse;
import com.example.tr.Mock.DTO.MockTwoResponse;
import com.example.tr.Mock.DTO.MockThreeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Menyimpan data yang diteruskan antar langkah orchestration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedDto {
    private MockOneResponse mock1Response;
    private MockTwoResponse mock2Response;
    private MockThreeResponse mock3Response;
}
// Ini adalah wadah (struktur data/objek) yang akan digunakan 
// oleh Harta, Christival, dan Yefta untuk saling mengoper 
// data antar-Langkah tanpa kebingungan.
