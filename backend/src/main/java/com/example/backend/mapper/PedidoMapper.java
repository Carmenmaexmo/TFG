package com.example.backend.mapper;

import com.example.backend.dto.*;
import com.example.backend.model.Pedido;
import com.example.backend.model.DetallePedido;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, ViniloMapper.class, DireccionEnvioMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoMapper {

    PedidoDTO toDTO(Pedido pedido);
    List<PedidoDTO> toDTOList(List<Pedido> pedidos);

    DetallePedidoDTO toDetalleDTO(DetallePedido detalle);
    List<DetallePedidoDTO> toDetalleDTOList(List<DetallePedido> detalles);
    
}
