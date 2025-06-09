package com.example.backend.mapper;

import com.example.backend.dto.AsistenciaEventoCreateDTO;
import com.example.backend.dto.AsistenciaEventoDTO;
import com.example.backend.model.AsistenciaEvento;

import org.mapstruct.*;

/**
 * Mapper de MapStruct para convertir entre entidades {@link AsistenciaEvento}
 * y sus correspondientes DTOs.
 */
@Mapper(
    componentModel = "spring",
    uses = {UsuarioMapper.class, EventoMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AsistenciaEventoMapper {

    /**
     * Convierte una entidad {@link AsistenciaEvento} a su DTO correspondiente.
     *
     * @param asistencia la entidad a convertir
     * @return el DTO resultante
     */
    AsistenciaEventoDTO toDTO(AsistenciaEvento asistencia);

    /**
     * Convierte un DTO de creación {@link AsistenciaEventoCreateDTO}
     * a una nueva entidad {@link AsistenciaEvento}.
     *
     * @param dto el DTO con los datos de entrada
     * @return la nueva entidad
     */
    @Mapping(target = "confirmado", source = "confirmado")
    AsistenciaEvento fromCreateDTO(AsistenciaEventoCreateDTO dto);
}
