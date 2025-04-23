package com.example.backend.mapper;

import com.example.backend.dto.UsuarioDTO;
import com.example.backend.dto.UsuarioCreateDTO;
import com.example.backend.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE) //Para ignorar los campos que no se mapean y que no me salga un warning
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDTO(Usuario usuario);

    Usuario fromCreateDTO(UsuarioCreateDTO dto);
}
