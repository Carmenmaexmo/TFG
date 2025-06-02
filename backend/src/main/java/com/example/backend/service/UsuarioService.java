package com.example.backend.service;

import com.example.backend.dto.UsuarioDTO;
import com.example.backend.dto.UsuarioUpdateDTO;
import com.example.backend.model.Usuario;
import com.example.backend.dto.UsuarioCreateDTO;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<UsuarioDTO> findAll();
    UsuarioDTO findById(Long id);
    UsuarioDTO create(UsuarioCreateDTO dto);
    UsuarioDTO partialUpdate(Long id, UsuarioUpdateDTO dto);
    void delete(Long id);
    Optional<Usuario> findByNombreUsuario(String username);
}
