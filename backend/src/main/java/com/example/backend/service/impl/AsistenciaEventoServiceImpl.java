// src/main/java/com/example/backend/service/impl/AsistenciaEventoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.AsistenciaEventoCreateDTO;
import com.example.backend.dto.AsistenciaEventoDTO;
import com.example.backend.dto.AsistenciaEventoUpdateDTO;
import com.example.backend.model.AsistenciaEvento;
import com.example.backend.model.Evento;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.AsistenciaEventoMapper;
import com.example.backend.repository.AsistenciaEventoRepository;
import com.example.backend.repository.EventoRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.AsistenciaEventoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional // ← Para que funcione la transacción los metodos publicos
@RequiredArgsConstructor
public class AsistenciaEventoServiceImpl implements AsistenciaEventoService {

    private final AsistenciaEventoRepository asistenciaRepo;
    private final UsuarioRepository usuarioRepo;
    private final EventoRepository eventoRepo;
    private final AsistenciaEventoMapper asistenciaMapper;

    @Override
    public List<AsistenciaEventoDTO> findAll() {
        return asistenciaRepo.findAll()
            .stream()
            .map(asistenciaMapper::toDTO)
            .toList();
    }

    @Override
    public AsistenciaEventoDTO findById(Long id) {
        AsistenciaEvento ae = asistenciaRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Asistencia no encontrada"
            ));
        return asistenciaMapper.toDTO(ae);
    }

    @Override
    public List<AsistenciaEventoDTO> findByEvento(Long idEvento) {
        return asistenciaRepo.findByEventoId(idEvento).stream()
            .map(asistenciaMapper::toDTO)
            .toList();
            
    }

    @Override
    public AsistenciaEventoDTO create(AsistenciaEventoCreateDTO dto) {
        // 1) Validar usuario y evento
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));
        Evento evento = eventoRepo.findById(dto.getIdEvento())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Evento no encontrado"
            ));

        // 2) Evitar duplicados
        boolean existe = asistenciaRepo
            .existsByUsuarioAndEvento(usuario, evento);
        if (existe) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El usuario ya está apuntado a este evento"
            );
        }

        // 3) Crear entidad
        AsistenciaEvento ae = AsistenciaEvento.builder()
            .usuario(usuario)
            .evento(evento)
            .confirmado(dto.getConfirmado() != null && dto.getConfirmado())
            .build();

        // 4) Guardar y devolver DTO
        return asistenciaMapper.toDTO(
            asistenciaRepo.save(ae)
        );
    }

    @Override
    public AsistenciaEventoDTO update(Long id, AsistenciaEventoUpdateDTO dto) {
        AsistenciaEvento ae = asistenciaRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Asistencia no encontrada"
            ));

        // 1) Usuario (opcional)
        if (dto.getIdUsuario() != null &&
            !dto.getIdUsuario().equals(ae.getUsuario().getIdUsuario())) {
            Usuario u = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
            ae.setUsuario(u);
        }

        // 2) Evento (opcional)
        if (dto.getIdEvento() != null &&
            !dto.getIdEvento().equals(ae.getEvento().getId())) {
            Evento e = eventoRepo.findById(dto.getIdEvento())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Evento no encontrado"
                ));
            ae.setEvento(e);
        }

        // 3) Confirmación (opcional)
        if (dto.getConfirmado() != null) {
            ae.setConfirmado(dto.getConfirmado());
        }

        // 4) Guardar y devolver DTO
        return asistenciaMapper.toDTO(
            asistenciaRepo.save(ae)
        );
    }

    @Override
    public void delete(Long id) {
        if (!asistenciaRepo.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Asistencia no existe"
            );
        }
        asistenciaRepo.deleteById(id);
    }

    @Override
    public AsistenciaEventoDTO confirmar(Long id) {
        AsistenciaEvento ae = asistenciaRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Asistencia no encontrada"
            ));
        ae.setConfirmado(true);
        return asistenciaMapper.toDTO(asistenciaRepo.save(ae));
    }
}
