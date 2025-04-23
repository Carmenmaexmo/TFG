package com.example.backend.service;

import com.example.backend.dto.DireccionEnvioDTO;
import com.example.backend.dto.DireccionEnvioCreateDTO;

import java.util.List;

public interface DireccionEnvioService {
    List<DireccionEnvioDTO> findAll();
    DireccionEnvioDTO findById(Long id);
    DireccionEnvioDTO create(DireccionEnvioCreateDTO dto);
    DireccionEnvioDTO update(Long id, DireccionEnvioCreateDTO dto);
    void delete(Long id);
}
