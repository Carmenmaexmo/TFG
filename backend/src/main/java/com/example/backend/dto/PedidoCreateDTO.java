package com.example.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoCreateDTO {
    private Long idUsuario;
    private Long idDireccionEnvio;
    private String estado;
    private Double total;
    private LocalDateTime fechaPedido;
    private List<DetallePedidoCreateDTO> detalles;
}
