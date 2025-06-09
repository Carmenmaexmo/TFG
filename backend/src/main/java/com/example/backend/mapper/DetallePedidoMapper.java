package com.example.backend.mapper;

import com.example.backend.dto.DetallePedidoDTO;
import com.example.backend.model.DetallePedido;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de MapStruct para la conversión entre entidades {@link DetallePedido}
 * y su correspondiente DTO {@link DetallePedidoDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {ViniloMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DetallePedidoMapper {

    /**
     * Convierte una entidad {@link DetallePedido} a su DTO correspondiente.
     *
     * @param detalle entidad del detalle de pedido
     * @return DTO con los datos del detalle
     */
    DetallePedidoDTO toDTO(DetallePedido detalle);
}
