package com.example.backend.mapper;

import com.example.backend.dto.ViniloDTO;
import com.example.backend.dto.ViniloCreateDTO;
import com.example.backend.model.Vinilo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ViniloMapper {

    ViniloDTO toDTO(Vinilo vinilo);

    Vinilo fromCreateDTO(ViniloCreateDTO dto);
}
