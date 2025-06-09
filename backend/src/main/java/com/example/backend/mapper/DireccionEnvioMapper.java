package com.example.backend.mapper;

import com.example.backend.dto.DireccionEnvioDTO;
import com.example.backend.dto.DireccionEnvioCreateDTO;
import com.example.backend.model.DireccionEnvio;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de MapStruct para la conversión entre entidades {@link DireccionEnvio}
 * y sus correspondientes DTOs.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DireccionEnvioMapper {

    /**
     * Convierte una entidad {@link DireccionEnvio} a su DTO correspondiente.
     *
     * @param direccion entidad de dirección de envío
     * @return DTO con los datos de la dirección
     */
    DireccionEnvioDTO toDTO(DireccionEnvio direccion);

    /**
     * Convierte un DTO de creación {@link DireccionEnvioCreateDTO}
     * en una entidad {@link DireccionEnvio}.
     *
     * @param dto DTO con los datos para crear la dirección
     * @return Entidad lista para persistencia
     */
    DireccionEnvio fromCreateDTO(DireccionEnvioCreateDTO dto);
}
