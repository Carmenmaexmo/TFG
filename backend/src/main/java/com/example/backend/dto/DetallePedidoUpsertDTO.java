package com.example.backend.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DetallePedidoUpsertDTO {
  private Long id;        
  private Long idVinilo;  
  private Double precio;
  private Integer cantidad;
}
