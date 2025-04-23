package com.example.backend.mapper;

import com.example.backend.dto.ProveedorDTO;
import com.example.backend.dto.ProveedorCreateDTO;
import com.example.backend.model.Proveedor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProveedorMapper {
    ProveedorDTO toDTO(Proveedor proveedor);
    Proveedor fromCreateDTO(ProveedorCreateDTO dto);
}
