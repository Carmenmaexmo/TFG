// src/main/java/com/example/backend/service/impl/BloqueoForoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.BloqueoForoCreateDTO;
import com.example.backend.dto.BloqueoForoDTO;
import com.example.backend.model.BloqueoForo;
import com.example.backend.model.Foro;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.BloqueoForoMapper;
import com.example.backend.repository.BloqueoForoRepository;
import com.example.backend.repository.ForoRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.BloqueoForoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloqueoForoServiceImpl implements BloqueoForoService {

    private final BloqueoForoRepository bloqueoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ForoRepository foroRepo;
    private final BloqueoForoMapper bloqueoMapper;

    @Override
    public List<BloqueoForoDTO> findAll() {
        return bloqueoRepo.findAll()
                .stream()
                .map(bloqueoMapper::toDTO)
                .toList();
    }

    @Override
    public BloqueoForoDTO findById(Long id) {
        BloqueoForo b = bloqueoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloqueo no encontrado"));
        return bloqueoMapper.toDTO(b);
    }

    @Override
    public BloqueoForoDTO create(BloqueoForoCreateDTO dto) {
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Foro foro = foroRepo.findById(dto.getIdForo())
                .orElseThrow(() -> new RuntimeException("Foro no encontrado"));

        BloqueoForo bloque = bloqueoMapper.fromCreateDTO(dto);
        bloque.setUsuario(usuario);
        bloque.setForo(foro);

        return bloqueoMapper.toDTO(bloqueoRepo.save(bloque));
    }

    @Override
    public BloqueoForoDTO update(Long id, BloqueoForoCreateDTO dto) {
        BloqueoForo b = bloqueoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Bloqueo no encontrado"));

        b.setMotivo(dto.getMotivo());
        // si quisiera permitir cambiar usuario o foro:
        // Usuario u = usuarioRepo.findById(dto.getIdUsuario()).orElseThrow(...);
        // b.setUsuario(u);

        return bloqueoMapper.toDTO(bloqueoRepo.save(b));
    }

    @Override
    public void delete(Long id) {
        bloqueoRepo.deleteById(id);
    }
}
