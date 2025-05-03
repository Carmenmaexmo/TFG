// src/main/java/com/example/backend/service/impl/EventoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.EventoCreateDTO;
import com.example.backend.dto.EventoDTO;
import com.example.backend.model.Evento;
import com.example.backend.mapper.EventoMapper;
import com.example.backend.repository.EventoRepository;
import com.example.backend.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;

    @Override
    public List<EventoDTO> findAll() {
        return eventoRepository.findAll()
                .stream()
                .map(eventoMapper::toDTO)
                .toList();
    }

    @Override
    public EventoDTO findById(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        return eventoMapper.toDTO(evento);
    }

    @Override
    public EventoDTO create(EventoCreateDTO dto) {
        Evento evento = eventoMapper.fromCreateDTO(dto);
        return eventoMapper.toDTO(eventoRepository.save(evento));
    }

    @Override
    public EventoDTO update(Long id, EventoCreateDTO dto) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        // Actualizamos campos
        evento.setTitulo(dto.getTitulo());
        evento.setDescripcion(dto.getDescripcion());
        evento.setFecha_inicio(dto.getFecha_inicio());
        evento.setFecha_fin(dto.getFecha_fin());
        evento.setLugar(dto.getLugar());
        return eventoMapper.toDTO(eventoRepository.save(evento));
    }

    @Override
    public void delete(Long id) {
        eventoRepository.deleteById(id);
    }
}
