// src/main/java/com/example/backend/service/impl/BloqueoForoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.BloqueoForoCreateDTO;
import com.example.backend.dto.BloqueoForoDTO;
import com.example.backend.dto.BloqueoForoUpdateDTO;
import com.example.backend.model.BloqueoForo;
import com.example.backend.model.Foro;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.BloqueoForoMapper;
import com.example.backend.repository.BloqueoForoRepository;
import com.example.backend.repository.ForoRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.BloqueoForoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        // 1) Validar usuario
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));

        // 2) Validar foro
        Foro foro = foroRepo.findById(dto.getIdForo())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Foro no encontrado"
            ));

        // 3) Validar motivo
        if (dto.getMotivo() == null || dto.getMotivo().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El motivo es obligatorio"
            );
        }

        // 4) Evitar duplicados (mismo usuario en mismo foro)
        if (bloqueoRepo.existsByUsuarioAndForo(usuario, foro)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Usuario ya bloqueado en este foro"
            );
        }

        // 5) Mapear, asignar relaciones y guardar
        BloqueoForo bloque = bloqueoMapper.fromCreateDTO(dto);
        bloque.setUsuario(usuario);
        bloque.setMotivo(dto.getMotivo());
        bloque.setFechaBloqueo(dto.getFechaBloqueo());
        bloque.setEstado(dto.getEstado());
        bloque.setForo(foro);

        return bloqueoMapper.toDTO(
            bloqueoRepo.save(bloque)
        );
    }


    @Override
    public BloqueoForoDTO update(Long id, BloqueoForoUpdateDTO dto) {
        BloqueoForo b = bloqueoRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Bloqueo no encontrado"
            ));

        // motivo (opcional)
        if (dto.getMotivo() != null) {
            if (dto.getMotivo().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El motivo no puede estar vacío"
                );
            }
            b.setMotivo(dto.getMotivo());
        }

        // cambiar usuario (opcional)
        if (dto.getIdUsuario() != null &&
            !dto.getIdUsuario().equals(b.getUsuario().getIdUsuario())) {
            Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
            if (bloqueoRepo.existsByUsuarioAndForo(usuario, b.getForo())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Usuario ya bloqueado en este foro"
                );
            }
            b.setUsuario(usuario);
        }

        // cambiar foro (opcional)
        if (dto.getIdForo() != null &&
            !dto.getIdForo().equals(b.getForo().getId())) {
            Foro foro = foroRepo.findById(dto.getIdForo())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Foro no encontrado"
                ));
            if (bloqueoRepo.existsByUsuarioAndForo(b.getUsuario(), foro)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Usuario ya bloqueado en este foro"
                );
            }
            b.setForo(foro);
        }

        return bloqueoMapper.toDTO(bloqueoRepo.save(b));
    }


    @Override
    public void delete(Long id) {
        bloqueoRepo.deleteById(id);
    }
}
