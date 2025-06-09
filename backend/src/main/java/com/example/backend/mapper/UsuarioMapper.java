package com.example.backend.mapper;

import com.example.backend.dto.UsuarioDTO;
import com.example.backend.dto.UsuarioCreateDTO;
import com.example.backend.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Mapper de MapStruct para la conversión entre la entidad {@link Usuario}
 * y sus respectivos DTOs.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE // Ignora los campos no mapeados para evitar advertencias
)
public interface UsuarioMapper {

    /**
     * Instancia del mapper generada automáticamente por MapStruct.
     */
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    /**
     * Convierte una entidad {@link Usuario} a su DTO correspondiente.
     *
     * @param usuario entidad del usuario
     * @return DTO del usuario
     */
    UsuarioDTO toDTO(Usuario usuario);

    /**
     * Convierte un DTO de creación {@link UsuarioCreateDTO} en una entidad {@link Usuario}.
     *
     * @param dto datos para crear un nuevo usuario
     * @return entidad Usuario lista para ser persistida
     */
    Usuario fromCreateDTO(UsuarioCreateDTO dto);
}
