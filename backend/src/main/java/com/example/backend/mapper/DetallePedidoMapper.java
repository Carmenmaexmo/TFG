package com.example.backend.mapper;

import com.example.backend.dto.DetallePedidoDTO;
import com.example.backend.model.DetallePedido;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {ViniloMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DetallePedidoMapper {
    DetallePedidoDTO toDTO(DetallePedido detalle);
}
