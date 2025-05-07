// src/main/java/com/example/backend/service/ForoService.java
package com.example.backend.service;

import com.example.backend.dto.ForoDTO;
import com.example.backend.dto.ForoUpdateDTO;
import com.example.backend.dto.ForoCreateDTO;

import java.util.List;

public interface ForoService {
    List<ForoDTO> findAll();
    ForoDTO findById(Long id);
    ForoDTO create(ForoCreateDTO dto);
    ForoDTO update(Long id, ForoUpdateDTO dto);
    void delete(Long id);
}
