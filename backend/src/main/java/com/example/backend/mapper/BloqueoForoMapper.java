package com.example.backend.mapper;

import com.example.backend.dto.BloqueoForoDTO;
import com.example.backend.dto.BloqueoForoCreateDTO;
import com.example.backend.model.BloqueoForo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, ForoMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BloqueoForoMapper {
    BloqueoForoDTO toDTO(BloqueoForo bloqueo);
    BloqueoForo fromCreateDTO(BloqueoForoCreateDTO dto);
}
