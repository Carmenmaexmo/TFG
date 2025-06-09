package com.example.backend.mapper;

import com.example.backend.dto.*;
import com.example.backend.model.Pedido;
import com.example.backend.model.DetallePedido;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper de MapStruct para la conversión entre entidades {@link Pedido} y {@link DetallePedido},
 * y sus correspondientes DTOs.
 */
@Mapper(
    componentModel = "spring",
    uses = {UsuarioMapper.class, ViniloMapper.class, DireccionEnvioMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PedidoMapper {

    /**
     * Convierte una entidad {@link Pedido} a su DTO correspondiente.
     *
     * @param pedido entidad del pedido
     * @return DTO con los datos del pedido
     */
    PedidoDTO toDTO(Pedido pedido);

    /**
     * Convierte una lista de entidades {@link Pedido} a una lista de DTOs.
     *
     * @param pedidos lista de entidades
     * @return lista de DTOs de pedido
     */
    List<PedidoDTO> toDTOList(List<Pedido> pedidos);

    /**
     * Convierte una entidad {@link DetallePedido} a su DTO correspondiente.
     *
     * @param detalle detalle del pedido
     * @return DTO del detalle del pedido
     */
    DetallePedidoDTO toDetalleDTO(DetallePedido detalle);

    /**
     * Convierte una lista de entidades {@link DetallePedido} a una lista de DTOs.
     *
     * @param detalles lista de detalles del pedido
     * @return lista de DTOs de detalles
     */
    List<DetallePedidoDTO> toDetalleDTOList(List<DetallePedido> detalles);
}
