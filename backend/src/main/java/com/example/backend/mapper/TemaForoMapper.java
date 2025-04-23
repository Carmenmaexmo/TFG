package com.example.backend.mapper;

import com.example.backend.dto.TemaForoDTO;
import com.example.backend.dto.TemaForoCreateDTO;
import com.example.backend.model.TemaForo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, ForoMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemaForoMapper {
    TemaForoDTO toDTO(TemaForo tema);
    TemaForo fromCreateDTO(TemaForoCreateDTO dto);
}
