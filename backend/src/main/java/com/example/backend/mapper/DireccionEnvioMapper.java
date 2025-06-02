package com.example.backend.mapper;

import com.example.backend.dto.DireccionEnvioDTO;
import com.example.backend.dto.DireccionEnvioCreateDTO;
import com.example.backend.model.DireccionEnvio;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DireccionEnvioMapper {

    DireccionEnvioDTO toDTO(DireccionEnvio direccion);
    

    DireccionEnvio fromCreateDTO(DireccionEnvioCreateDTO dto);
}
