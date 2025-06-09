// src/main/java/com/example/backend/controller/BloqueoForoController.java
package com.example.backend.controller;

import com.example.backend.dto.BloqueoForoCreateDTO;
import com.example.backend.dto.BloqueoForoDTO;
import com.example.backend.dto.BloqueoForoUpdateDTO;
import com.example.backend.service.BloqueoForoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de bloqueos de temas en el foro.
 */
@RestController
@RequestMapping("/api/bloqueos-foro")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Bloqueos de Foro", description = "Operaciones para bloquear y gestionar el estado de los temas del foro")
public class BloqueoForoController {

    private final BloqueoForoService bloqueoService;

    @GetMapping
    @Operation(summary = "Listar bloqueos", description = "Devuelve todos los bloqueos de temas registrados en el foro")
    public List<BloqueoForoDTO> getAll() {
        return bloqueoService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar bloqueo por ID", description = "Obtiene un bloqueo de tema específico mediante su ID")
    public BloqueoForoDTO getById(@PathVariable Long id) {
        return bloqueoService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear bloqueo", description = "Registra un nuevo bloqueo para un tema del foro")
    public BloqueoForoDTO create(@RequestBody BloqueoForoCreateDTO dto) {
        return bloqueoService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar bloqueo", description = "Modifica un bloqueo existente mediante su ID")
    public BloqueoForoDTO update(@PathVariable Long id, @RequestBody BloqueoForoUpdateDTO dto) {
        return bloqueoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar bloqueo", description = "Elimina un bloqueo de tema mediante su ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bloqueoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
