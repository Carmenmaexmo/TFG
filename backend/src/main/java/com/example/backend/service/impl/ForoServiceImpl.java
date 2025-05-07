// src/main/java/com/example/backend/service/impl/ForoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.ForoCreateDTO;
import com.example.backend.dto.ForoDTO;
import com.example.backend.dto.ForoUpdateDTO;
import com.example.backend.model.Foro;
import com.example.backend.mapper.ForoMapper;
import com.example.backend.repository.ForoRepository;
import com.example.backend.service.ForoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        // 1) Nombre obligatorio y no duplicado
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El nombre del foro es obligatorio"
            );
        }
        String nombreTrim = dto.getNombre().trim();
        if (foroRepo.existsByNombre(nombreTrim)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Ya existe un foro con ese nombre"
            );
        }

        // 2) Descripción obligatoria
        if (dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La descripción es obligatoria"
            );
        }

        // 3) Crear y guardar
        Foro foro = foroMapper.fromCreateDTO(dto);
        foro.setNombre(nombreTrim);
        Foro guardado = foroRepo.save(foro);
        return foroMapper.toDTO(guardado);
    }

    @Override
    public ForoDTO update(Long id, ForoUpdateDTO dto) {
        Foro foro = foroRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Foro no encontrado"
            ));

        // 1) Nombre (si viene en el DTO)
        if (dto.getNombre() != null) {
            String nombreTrim = dto.getNombre().trim();
            if (nombreTrim.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El nombre no puede estar vacío"
                );
            }
            if (!nombreTrim.equals(foro.getNombre()) && foroRepo.existsByNombre(nombreTrim)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ya existe un foro con ese nombre"
                );
            }
            foro.setNombre(nombreTrim);
        }

        // 2) Descripción (si viene en el DTO)
        if (dto.getDescripcion() != null) {
            String descTrim = dto.getDescripcion().trim();
            if (descTrim.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La descripción no puede estar vacía"
                );
            }
            foro.setDescripcion(descTrim);
        }

        Foro actualizado = foroRepo.save(foro);
        return foroMapper.toDTO(actualizado);
    }

    @Override
    public void delete(Long id) {
        foroRepo.deleteById(id);
    }
}
