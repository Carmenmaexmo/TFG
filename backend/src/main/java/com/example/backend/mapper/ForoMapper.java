package com.example.backend.mapper;

import com.example.backend.dto.ForoDTO;
import com.example.backend.dto.ForoCreateDTO;
import com.example.backend.model.Foro;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de MapStruct para la conversión entre la entidad {@link Foro}
 * y sus correspondientes DTOs.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ForoMapper {

    /**
     * Convierte una entidad {@link Foro} a su DTO correspondiente.
     *
     * @param foro entidad del foro
     * @return DTO con los datos del foro
     */
    ForoDTO toDTO(Foro foro);

    /**
     * Convierte un DTO de creación {@link ForoCreateDTO}
     * en una entidad {@link Foro}.
     *
     * @param dto DTO con los datos necesarios para crear un foro
     * @return entidad lista para persistencia
     */
    Foro fromCreateDTO(ForoCreateDTO dto);
}
