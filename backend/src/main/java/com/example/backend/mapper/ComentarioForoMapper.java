package com.example.backend.mapper;

import com.example.backend.dto.ComentarioForoDTO;
import com.example.backend.dto.ComentarioForoCreateDTO;
import com.example.backend.model.ComentarioForo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, TemaForoMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComentarioForoMapper {
    ComentarioForoDTO toDTO(ComentarioForo comentario);
    ComentarioForo fromCreateDTO(ComentarioForoCreateDTO dto);
}
