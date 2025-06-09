package com.example.backend.mapper;

import com.example.backend.dto.ProveedorDTO;
import com.example.backend.dto.ProveedorCreateDTO;
import com.example.backend.model.Proveedor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de MapStruct para la conversión entre la entidad {@link Proveedor}
 * y sus respectivos DTOs.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProveedorMapper {

    /**
     * Convierte una entidad {@link Proveedor} a su DTO correspondiente.
     *
     * @param proveedor entidad del proveedor
     * @return DTO del proveedor
     */
    ProveedorDTO toDTO(Proveedor proveedor);

    /**
     * Convierte un DTO de creación {@link ProveedorCreateDTO} en una entidad {@link Proveedor}.
     *
     * @param dto datos para crear un proveedor
     * @return entidad proveedor lista para persistencia
     */
    Proveedor fromCreateDTO(ProveedorCreateDTO dto);
}
