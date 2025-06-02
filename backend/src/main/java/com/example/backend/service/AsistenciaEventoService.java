// src/main/java/com/example/backend/service/AsistenciaEventoService.java
package com.example.backend.service;

import com.example.backend.dto.AsistenciaEventoDTO;
import com.example.backend.dto.AsistenciaEventoUpdateDTO;
import com.example.backend.dto.AsistenciaEventoCreateDTO;

import java.util.List;

public interface AsistenciaEventoService {
    List<AsistenciaEventoDTO> findAll();
    AsistenciaEventoDTO findById(Long id);
    AsistenciaEventoDTO create(AsistenciaEventoCreateDTO dto);
    AsistenciaEventoDTO update(Long id, AsistenciaEventoUpdateDTO dto);
    void delete(Long id);
    AsistenciaEventoDTO confirmar(Long id);
    List<AsistenciaEventoDTO> findByEvento(Long idEvento);
}
