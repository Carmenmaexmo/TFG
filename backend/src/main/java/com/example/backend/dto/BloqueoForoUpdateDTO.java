package com.example.backend.dto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloqueoForoUpdateDTO {
    private Long idUsuario;  
    private Long idForo; 
    private LocalDateTime fechaBloqueo;
    private String estado;
    private String motivo;    
}
