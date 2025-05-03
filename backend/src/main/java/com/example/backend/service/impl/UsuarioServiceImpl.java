package com.example.backend.service.impl;

import com.example.backend.dto.UsuarioDTO;
import com.example.backend.dto.UsuarioCreateDTO;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.UsuarioMapper;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    @Override
    public UsuarioDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    public UsuarioDTO create(UsuarioCreateDTO dto) {
        Usuario usuario = usuarioMapper.fromCreateDTO(dto);
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioDTO update(Long id, UsuarioCreateDTO dto) {
        Usuario u = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Copiamos los campos editables
        u.setNombre(dto.getNombre());
        u.setApellidos(dto.getApellidos());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setTelefono(dto.getTelefono());
        u.setDni(dto.getDni());
        // (El rol y el carrito posiblemente no se editen aquí)

        return usuarioMapper.toDTO(usuarioRepository.save(u));
    }

    @Override
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no existe");
        }
        usuarioRepository.deleteById(id);
    }
}
