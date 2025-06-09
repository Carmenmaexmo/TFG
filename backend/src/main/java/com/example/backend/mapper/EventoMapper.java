package com.example.backend.mapper;

import com.example.backend.dto.EventoDTO;
import com.example.backend.dto.EventoCreateDTO;
import com.example.backend.model.Evento;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de MapStruct para la conversión entre entidades {@link Evento}
 * y sus correspondientes DTOs.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EventoMapper {

    /**
     * Convierte una entidad {@link Evento} a su DTO correspondiente.
     *
     * @param evento entidad del evento
     * @return DTO representando el evento
     */
    EventoDTO toDTO(Evento evento);

    /**
     * Convierte un DTO de creación {@link EventoCreateDTO}
     * en una entidad {@link Evento}.
     *
     * @param dto DTO con los datos del nuevo evento
     * @return Entidad lista para ser persistida
     */
    Evento fromCreateDTO(EventoCreateDTO dto);
}
