package com.example.backend.mapper;

import com.example.backend.dto.ComentarioForoDTO;
import com.example.backend.dto.ComentarioForoCreateDTO;
import com.example.backend.model.ComentarioForo;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de MapStruct para la conversión entre entidades {@link ComentarioForo}
 * y sus correspondientes DTOs.
 */
@Mapper(
    componentModel = "spring",
    uses = {UsuarioMapper.class, TemaForoMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ComentarioForoMapper {

    /**
     * Convierte una entidad {@link ComentarioForo} a su DTO correspondiente.
     *
     * @param comentario entidad del comentario
     * @return DTO representando el comentario
     */
    ComentarioForoDTO toDTO(ComentarioForo comentario);

    /**
     * Convierte un DTO de creación {@link ComentarioForoCreateDTO}
     * en una entidad {@link ComentarioForo}.
     *
     * @param dto DTO con los datos del nuevo comentario
     * @return Entidad lista para ser persistida
     */
    ComentarioForo fromCreateDTO(ComentarioForoCreateDTO dto);
}
