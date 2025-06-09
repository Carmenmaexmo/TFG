// Implementación del servicio de eventos, con validaciones estrictas y control de lógica de negocio.
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

@Service // Marca esta clase como un servicio gestionado por Spring
@RequiredArgsConstructor // Lombok genera el constructor con todos los campos final
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;

    /**
     * Devuelve todos los eventos disponibles en la base de datos.
     */
    @Override
    public List<EventoDTO> findAll() {
        return eventoRepository.findAll()
                .stream()
                .map(eventoMapper::toDTO)
                .toList();
    }

    /**
     * Busca un evento por su ID. Lanza 404 si no se encuentra.
     */
    @Override
    public EventoDTO findById(Long id) {
        Evento ev = eventoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Evento no encontrado"
            ));
        return eventoMapper.toDTO(ev);
    }

    /**
     * Crea un nuevo evento validando título, fechas, lugar y unicidad.
     */
    @Override
    public EventoDTO create(EventoCreateDTO dto) {
        // Validación del título
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "El título es obligatorio");
        }
        String titulo = dto.getTitulo().trim();

        // Validación de fechas
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

        // Validación del lugar
        if (dto.getLugar() == null || dto.getLugar().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "El lugar es obligatorio");
        }
        String lugar = dto.getLugar().trim();

        // Comprobación de unicidad: mismo título y misma hora
        if (eventoRepository.existsByTituloAndFechaInicio(titulo, inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Ya existe un evento con ese título a esa hora de inicio");
        }

        // Persistencia del evento
        Evento ev = eventoMapper.fromCreateDTO(dto);
        ev.setFechaInicio(inicio);
        ev.setFecha_fin(fin);
        ev.setTitulo(titulo);
        ev.setLugar(lugar);
        return eventoMapper.toDTO(eventoRepository.save(ev));
    }

    /**
     * Actualiza un evento existente de forma parcial, con validaciones consistentes.
     */
    @Override
    public EventoDTO update(Long id, EventoUpdateDTO dto) {
        Evento ev = eventoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Evento no encontrado"
            ));

        // Actualizar título si viene
        if (dto.getTitulo() != null) {
            String t = dto.getTitulo().trim();
            if (t.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El título no puede estar vacío");
            }
            // Comprobar si otro evento ya tiene ese título en esa fecha
            if (!t.equals(ev.getTitulo())
                && eventoRepository.existsByTituloAndFechaInicio(t, ev.getFechaInicio())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe otro evento con ese título a esa hora de inicio");
            }
            ev.setTitulo(t);
        }

        // Actualizar descripción
        if (dto.getDescripcion() != null) {
            ev.setDescripcion(dto.getDescripcion().trim());
        }

        // Actualizar fecha de inicio
        if (dto.getFechaInicio() != null) {
            LocalDateTime ni = dto.getFechaInicio();
            if (ni.isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de inicio debe ser futura");
            }
            ev.setFechaInicio(ni);
        }

        // Actualizar fecha de fin
        if (dto.getFecha_fin() != null) {
            LocalDateTime nf = dto.getFecha_fin();
            LocalDateTime actualInicio = ev.getFechaInicio();
            if (actualInicio != null && !nf.isAfter(actualInicio)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de fin debe ser posterior a la de inicio");
            }
            ev.setFecha_fin(nf);
        }

        // Actualizar lugar
        if (dto.getLugar() != null) {
            String l = dto.getLugar().trim();
            if (l.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El lugar no puede estar vacío");
            }
            ev.setLugar(l);
        }

        // Guardar los cambios
        return eventoMapper.toDTO(eventoRepository.save(ev));
    }

    /**
     * Elimina un evento por ID si existe, lanza 404 en caso contrario.
     */
    @Override
    public void delete(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Evento no existe");
        }
        eventoRepository.deleteById(id);
    }
}
