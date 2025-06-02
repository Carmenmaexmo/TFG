package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoDTO {
    private Long id;
    private Integer cantidad;
    private Double precio;
    private ViniloDTO vinilo;
}
