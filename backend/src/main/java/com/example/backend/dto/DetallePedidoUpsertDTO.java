package com.example.backend.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DetallePedidoUpsertDTO {
  private Long id;        // null si es nuevo
  private Long idVinilo;  // el identificador del vinilo
  private Integer cantidad;
}
