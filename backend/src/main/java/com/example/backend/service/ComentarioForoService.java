// src/main/java/com/example/backend/service/ComentarioForoService.java
package com.example.backend.service;

import com.example.backend.dto.ComentarioForoDTO;
import com.example.backend.dto.ComentarioForoCreateDTO;

import java.util.List;

public interface ComentarioForoService {
    List<ComentarioForoDTO> findAll();
    ComentarioForoDTO findById(Long id);
    ComentarioForoDTO create(ComentarioForoCreateDTO dto);
    ComentarioForoDTO update(Long id, ComentarioForoCreateDTO dto);
    void delete(Long id);
}
