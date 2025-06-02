// src/main/java/com/example/backend/service/impl/EventoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.EventoCreateDTO;
import com.example.backend.dto.EventoDTO;
import com.example.backend.dto.EventoUpdateDTO;
import com.example.backend.model.Evento;
import com.example.backend.mapper.EventoMapper;
import com.example.backend.repository.EventoRepository;
import com.example.backend.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
        Evento ev = eventoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Evento no encontrado"
            ));
        return eventoMapper.toDTO(ev);
    }

    @Override
    public EventoDTO create(EventoCreateDTO dto) {
        // 1) Validar título
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "El título es obligatorio");
        }
        String titulo = dto.getTitulo().trim();

        // 2) Validar fechas
        LocalDateTime inicio = dto.getFechaInicio();
        LocalDateTime fin    = dto.getFecha_fin();
        if (inicio == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "La fecha de inicio es obligatoria");
        }
        if (fin == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "La fecha de fin es obligatoria");
        }
        if (inicio.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "La fecha de inicio debe ser futura");
        }
        if (!fin.isAfter(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "La fecha de fin debe ser posterior a la de inicio");
        }

        // 3) Validar lugar
        if (dto.getLugar() == null || dto.getLugar().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "El lugar es obligatorio");
        }
        String lugar = dto.getLugar().trim();

        // 4) Unicidad: mismo título y hora de inicio
        if (eventoRepository.existsByTituloAndFechaInicio(titulo, inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Ya existe un evento con ese título a esa hora de inicio");
        }

        // 5) Persistir
        Evento ev = eventoMapper.fromCreateDTO(dto);
        ev.setFechaInicio(inicio);
        ev.setFecha_fin(fin);
        ev.setTitulo(titulo);
        ev.setLugar(lugar);
        return eventoMapper.toDTO(eventoRepository.save(ev));
    }

    @Override
    public EventoDTO update(Long id, EventoUpdateDTO dto) {
        Evento ev = eventoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Evento no encontrado"
            ));

        // Título (si viene)
        if (dto.getTitulo() != null) {
            String t = dto.getTitulo().trim();
            if (t.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El título no puede estar vacío");
            }
            if (!t.equals(ev.getTitulo())
                && eventoRepository.existsByTituloAndFechaInicio(t, ev.getFechaInicio())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe otro evento con ese título a esa hora de inicio");
            }
            ev.setTitulo(t);
        }

        // Descripción
        if (dto.getDescripcion() != null) {
            ev.setDescripcion(dto.getDescripcion().trim());
        }

        // Fecha de inicio
        if (dto.getFechaInicio() != null) {
            LocalDateTime ni = dto.getFechaInicio();
            if (ni.isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de inicio debe ser futura");
            }
            ev.setFechaInicio(ni);
        }

        // Fecha de fin
        if (dto.getFecha_fin() != null) {
            LocalDateTime nf = dto.getFecha_fin();
            LocalDateTime actualInicio = ev.getFechaInicio();
            if (actualInicio != null && !nf.isAfter(actualInicio)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de fin debe ser posterior a la de inicio");
            }
            ev.setFecha_fin(nf);
        }

        // Lugar
        if (dto.getLugar() != null) {
            String l = dto.getLugar().trim();
            if (l.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El lugar no puede estar vacío");
            }
            ev.setLugar(l);
        }

        return eventoMapper.toDTO(eventoRepository.save(ev));
    }

    @Override
    public void delete(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Evento no existe");
        }
        eventoRepository.deleteById(id);
    }
}
