// src/main/java/com/example/backend/controller/AsistenciaEventoController.java
package com.example.backend.controller;

import com.example.backend.dto.AsistenciaEventoCreateDTO;
import com.example.backend.dto.AsistenciaEventoDTO;
import com.example.backend.dto.AsistenciaEventoUpdateDTO;
import com.example.backend.service.AsistenciaEventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asistencias-evento")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AsistenciaEventoController {

    private final AsistenciaEventoService asistenciaService;

    @GetMapping
    public List<AsistenciaEventoDTO> getAll() {
        return asistenciaService.findAll();
    }

    @GetMapping("/{id}")
    public AsistenciaEventoDTO getById(@PathVariable Long id) {
        return asistenciaService.findById(id);
    }

    @GetMapping("/por-evento/{idEvento}")
    public List<AsistenciaEventoDTO> getByEvento(@PathVariable Long idEvento) {
        return asistenciaService.findByEvento(idEvento);
    }

    @PostMapping
    public AsistenciaEventoDTO create(@RequestBody AsistenciaEventoCreateDTO dto) {
        return asistenciaService.create(dto);
    }

    @PutMapping("/{id}")
    public AsistenciaEventoDTO update(
            @PathVariable Long id,
            @RequestBody AsistenciaEventoUpdateDTO dto
    ) {
        return asistenciaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        asistenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
