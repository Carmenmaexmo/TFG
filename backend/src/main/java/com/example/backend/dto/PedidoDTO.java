package com.example.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {
    private Long id;
    private LocalDateTime fecha;
    private Double total;
    private String estado;
    private UsuarioDTO usuario;
    private DireccionEnvioDTO direccionEnvio;
    private List<DetallePedidoDTO> detalles;
}
