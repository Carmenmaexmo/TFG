// src/main/java/com/example/backend/service/impl/AsistenciaEventoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.AsistenciaEventoDTO;
import com.example.backend.dto.AsistenciaEventoCreateDTO;
import com.example.backend.model.AsistenciaEvento;
import com.example.backend.model.Evento;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.AsistenciaEventoMapper;
import com.example.backend.repository.AsistenciaEventoRepository;
import com.example.backend.repository.EventoRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.AsistenciaEventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
        return asistenciaMapper.toDTO(ae);
    }

    @Override
    public AsistenciaEventoDTO create(AsistenciaEventoCreateDTO dto) {
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Evento evento = eventoRepo.findById(dto.getIdEvento())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        AsistenciaEvento ae = AsistenciaEvento.builder()
                .usuario(usuario)
                .evento(evento)
                .confirmado(dto.getConfirmado() != null ? dto.getConfirmado() : Boolean.FALSE)
                .build();

        return asistenciaMapper.toDTO(asistenciaRepo.save(ae));
    }

    @Override
    public AsistenciaEventoDTO update(Long id, AsistenciaEventoCreateDTO dto) {
        AsistenciaEvento ae = asistenciaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));

        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Evento evento = eventoRepo.findById(dto.getIdEvento())
            .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        ae.setUsuario(usuario);
        ae.setEvento(evento);

        return asistenciaMapper.toDTO(asistenciaRepo.save(ae));
    }

    @Override
    public void delete(Long id) {
        asistenciaRepo.deleteById(id);
    }

    @Override
    public AsistenciaEventoDTO confirmar(Long id) {
        AsistenciaEvento ae = asistenciaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
        ae.setConfirmado(true);
        return asistenciaMapper.toDTO(asistenciaRepo.save(ae));
    }
}
