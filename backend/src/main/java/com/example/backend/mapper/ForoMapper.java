package com.example.backend.mapper;

import com.example.backend.dto.ForoDTO;
import com.example.backend.dto.ForoCreateDTO;
import com.example.backend.model.Foro;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ForoMapper {
    ForoDTO toDTO(Foro foro);
    Foro fromCreateDTO(ForoCreateDTO dto);
}
