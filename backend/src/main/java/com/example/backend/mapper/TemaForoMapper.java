package com.example.backend.mapper;

import com.example.backend.dto.TemaForoDTO;
import com.example.backend.dto.TemaForoCreateDTO;
import com.example.backend.model.TemaForo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de MapStruct para convertir entre la entidad {@link TemaForo}
 * y sus correspondientes DTOs.
 */
@Mapper(
    componentModel = "spring",
    uses = {UsuarioMapper.class, ForoMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TemaForoMapper {

    /**
     * Convierte una entidad {@link TemaForo} en su representación DTO.
     *
     * @param tema entidad del tema de foro
     * @return DTO del tema de foro
     */
    TemaForoDTO toDTO(TemaForo tema);

    /**
     * Convierte un DTO de creación {@link TemaForoCreateDTO} en una entidad {@link TemaForo}.
     *
     * @param dto datos para crear un tema de foro
     * @return entidad TemaForo lista para guardar
     */
    TemaForo fromCreateDTO(TemaForoCreateDTO dto);
}
