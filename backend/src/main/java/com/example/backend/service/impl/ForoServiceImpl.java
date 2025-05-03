// src/main/java/com/example/backend/service/impl/ForoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.ForoCreateDTO;
import com.example.backend.dto.ForoDTO;
import com.example.backend.model.Foro;
import com.example.backend.mapper.ForoMapper;
import com.example.backend.repository.ForoRepository;
import com.example.backend.service.ForoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForoServiceImpl implements ForoService {

    private final ForoRepository foroRepo;
    private final ForoMapper foroMapper;

    @Override
    public List<ForoDTO> findAll() {
        return foroRepo.findAll()
                .stream()
                .map(foroMapper::toDTO)
                .toList();
    }

    @Override
    public ForoDTO findById(Long id) {
        Foro foro = foroRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Foro no encontrado"));
        return foroMapper.toDTO(foro);
    }

    @Override
    public ForoDTO create(ForoCreateDTO dto) {
        Foro foro = foroMapper.fromCreateDTO(dto);
        return foroMapper.toDTO(foroRepo.save(foro));
    }

    @Override
    public ForoDTO update(Long id, ForoCreateDTO dto) {
        Foro foro = foroRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Foro no encontrado"));
        foro.setNombre(dto.getNombre());
        foro.setDescripcion(dto.getDescripcion());
        return foroMapper.toDTO(foroRepo.save(foro));
    }

    @Override
    public void delete(Long id) {
        foroRepo.deleteById(id);
    }
}
