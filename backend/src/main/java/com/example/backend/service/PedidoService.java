package com.example.backend.service;

import com.example.backend.dto.PedidoDTO;
import com.example.backend.dto.PedidoCreateDTO;

import java.util.List;

public interface PedidoService {
    List<PedidoDTO> findAll();
    PedidoDTO findById(Long id);
    PedidoDTO create(PedidoCreateDTO dto);
    void delete(Long id);
}
