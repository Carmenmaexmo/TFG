// Implementación del servicio de gestión de bloqueos de usuarios en foros
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

@Service // Marca esta clase como un servicio gestionado por Spring
@RequiredArgsConstructor // Inyección de dependencias automática a través de constructor
public class BloqueoForoServiceImpl implements BloqueoForoService {

    private final BloqueoForoRepository bloqueoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ForoRepository foroRepo;
    private final BloqueoForoMapper bloqueoMapper;

    /**
     * Devuelve la lista completa de bloqueos en el sistema.
     */
    @Override
    public List<BloqueoForoDTO> findAll() {
        return bloqueoRepo.findAll()
                .stream()
                .map(bloqueoMapper::toDTO)
                .toList();
    }

    /**
     * Busca un bloqueo específico por ID. Lanza excepción si no existe.
     */
    @Override
    public BloqueoForoDTO findById(Long id) {
        BloqueoForo b = bloqueoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloqueo no encontrado"));
        return bloqueoMapper.toDTO(b);
    }

    /**
     * Crea un nuevo registro de bloqueo para un usuario en un foro.
     * Valida usuario, foro, motivo y evita duplicados.
     */
    @Override
    public BloqueoForoDTO create(BloqueoForoCreateDTO dto) {
        // 1) Validación del usuario
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));

        // 2) Validación del foro
        Foro foro = foroRepo.findById(dto.getIdForo())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Foro no encontrado"
            ));

        // 3) Validación del motivo
        if (dto.getMotivo() == null || dto.getMotivo().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El motivo es obligatorio"
            );
        }

        // 4) Comprobación de bloqueo duplicado
        if (bloqueoRepo.existsByUsuarioAndForo(usuario, foro)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Usuario ya bloqueado en este foro"
            );
        }

        // 5) Crear entidad, asignar relaciones y guardar
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

    /**
     * Actualiza los datos de un bloqueo existente. Permite modificar
     * motivo, usuario o foro si son distintos y válidos.
     */
    @Override
    public BloqueoForoDTO update(Long id, BloqueoForoUpdateDTO dto) {
        BloqueoForo b = bloqueoRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Bloqueo no encontrado"
            ));

        // Actualización del motivo si se proporciona
        if (dto.getMotivo() != null) {
            if (dto.getMotivo().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El motivo no puede estar vacío"
                );
            }
            b.setMotivo(dto.getMotivo());
        }

        // Cambio de usuario si es distinto al actual
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

        // Cambio de foro si es distinto al actual
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

    /**
     * Elimina un bloqueo por su ID. No lanza excepción si no existe.
     */
    @Override
    public void delete(Long id) {
        bloqueoRepo.deleteById(id);
    }
}
