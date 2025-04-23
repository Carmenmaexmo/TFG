package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoCreateDTO {
    private Long idVinilo;
    private Integer cantidad;
}
