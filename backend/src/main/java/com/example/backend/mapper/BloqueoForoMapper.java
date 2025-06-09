package com.example.backend.mapper;

import com.example.backend.dto.BloqueoForoDTO;
import com.example.backend.dto.BloqueoForoCreateDTO;
import com.example.backend.model.BloqueoForo;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de MapStruct para la conversión entre entidades {@link BloqueoForo}
 * y sus correspondientes DTOs.
 */
@Mapper(
    componentModel = "spring",
    uses = {UsuarioMapper.class, ForoMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BloqueoForoMapper {

    /**
     * Convierte una entidad {@link BloqueoForo} a su DTO correspondiente.
     *
     * @param bloqueo entidad del bloqueo del foro
     * @return DTO representando el bloqueo
     */
    BloqueoForoDTO toDTO(BloqueoForo bloqueo);

    /**
     * Convierte un DTO de creación {@link BloqueoForoCreateDTO}
     * en una entidad {@link BloqueoForo}.
     *
     * @param dto DTO con los datos del nuevo bloqueo
     * @return Entidad lista para ser persistida
     */
    BloqueoForo fromCreateDTO(BloqueoForoCreateDTO dto);
}
