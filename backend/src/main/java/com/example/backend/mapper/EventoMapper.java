package com.example.backend.mapper;

import com.example.backend.dto.EventoDTO;
import com.example.backend.dto.EventoCreateDTO;
import com.example.backend.model.Evento;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventoMapper {

    EventoDTO toDTO(Evento evento);

    Evento fromCreateDTO(EventoCreateDTO dto);
}
