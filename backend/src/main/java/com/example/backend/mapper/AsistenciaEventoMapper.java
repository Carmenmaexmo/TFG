package com.example.backend.mapper;

import com.example.backend.dto.AsistenciaEventoCreateDTO;
import com.example.backend.dto.AsistenciaEventoDTO;
import com.example.backend.model.AsistenciaEvento;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, EventoMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AsistenciaEventoMapper {

    AsistenciaEventoDTO toDTO(AsistenciaEvento asistencia);

    @Mapping(target = "confirmado", source = "confirmado")
    AsistenciaEvento fromCreateDTO(AsistenciaEventoCreateDTO dto);
}
