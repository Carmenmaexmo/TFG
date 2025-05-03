// src/main/java/com/example/backend/service/BloqueoForoService.java
package com.example.backend.service;

import com.example.backend.dto.BloqueoForoDTO;
import com.example.backend.dto.BloqueoForoCreateDTO;

import java.util.List;

public interface BloqueoForoService {
    List<BloqueoForoDTO> findAll();
    BloqueoForoDTO findById(Long id);
    BloqueoForoDTO create(BloqueoForoCreateDTO dto);
    BloqueoForoDTO update(Long id, BloqueoForoCreateDTO dto);
    void delete(Long id);
}
