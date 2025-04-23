package com.example.backend.service;

import com.example.backend.dto.ViniloDTO;
import com.example.backend.dto.ViniloCreateDTO;

import java.util.List;

public interface ViniloService {
    List<ViniloDTO> findAll();
    ViniloDTO findById(Long id);
    ViniloDTO create(ViniloCreateDTO dto);
    ViniloDTO update(Long id, ViniloCreateDTO dto);
    void delete(Long id);
}
