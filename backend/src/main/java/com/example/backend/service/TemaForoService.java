// src/main/java/com/example/backend/service/TemaForoService.java
package com.example.backend.service;

import com.example.backend.dto.TemaForoDTO;
import com.example.backend.dto.TemaForoUpdateDTO;
import com.example.backend.dto.TemaForoCreateDTO;

import java.util.List;

public interface TemaForoService {
    List<TemaForoDTO> findAll();
    TemaForoDTO findById(Long id);
    TemaForoDTO create(TemaForoCreateDTO dto);
    TemaForoDTO update(Long id, TemaForoUpdateDTO dto);
    void delete(Long id);
}
