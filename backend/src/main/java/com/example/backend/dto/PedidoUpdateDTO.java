// src/main/java/com/example/backend/dto/PedidoUpdateDTO.java
package com.example.backend.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoUpdateDTO {
    private Long idUsuario;
    private Long idDireccionEnvio;
    private String estado;
    private LocalDateTime fechaPedido;
    private Double total;
    private List<DetallePedidoUpsertDTO> detalles;
}
