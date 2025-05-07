package com.example.backend.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProveedorUpdateDTO {
    private String nombre;   
    private String email;     
    private String telefono; 
    private String direccion; 
}