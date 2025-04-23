package com.example.backend.service;

import com.example.backend.dto.ProveedorDTO;
import com.example.backend.dto.ProveedorCreateDTO;

import java.util.List;

public interface ProveedorService {
    List<ProveedorDTO> findAll();
    ProveedorDTO findById(Long id);
    ProveedorDTO create(ProveedorCreateDTO dto);
    ProveedorDTO update(Long id, ProveedorCreateDTO dto);
    void delete(Long id);
}
