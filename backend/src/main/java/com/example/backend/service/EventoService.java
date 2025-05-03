// src/main/java/com/example/backend/service/EventoService.java
package com.example.backend.service;

import com.example.backend.dto.EventoDTO;
import com.example.backend.dto.EventoCreateDTO;

import java.util.List;

public interface EventoService {
    List<EventoDTO> findAll();
    EventoDTO findById(Long id);
    EventoDTO create(EventoCreateDTO dto);
    EventoDTO update(Long id, EventoCreateDTO dto);
    void delete(Long id);
}
