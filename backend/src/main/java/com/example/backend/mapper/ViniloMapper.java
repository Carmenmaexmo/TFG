package com.example.backend.mapper;

import com.example.backend.dto.ViniloDTO;
import com.example.backend.dto.ViniloCreateDTO;
import com.example.backend.model.Vinilo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de MapStruct para convertir entre entidades {@link Vinilo}
 * y sus DTOs correspondientes.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE // Ignora campos no mapeados para evitar advertencias
)
public interface ViniloMapper {

    /**
     * Convierte una entidad {@link Vinilo} en un {@link ViniloDTO}.
     *
     * @param vinilo entidad de vinilo
     * @return DTO representando el vinilo
     */
    ViniloDTO toDTO(Vinilo vinilo);

    /**
     * Convierte un {@link ViniloCreateDTO} en una entidad {@link Vinilo}.
     *
     * @param dto DTO con los datos de creación del vinilo
     * @return entidad lista para persistencia
     */
    Vinilo fromCreateDTO(ViniloCreateDTO dto);
}
