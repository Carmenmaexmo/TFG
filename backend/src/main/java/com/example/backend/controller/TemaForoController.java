// src/main/java/com/example/backend/controller/TemaForoController.java
package com.example.backend.controller;

import com.example.backend.dto.TemaForoCreateDTO;
import com.example.backend.dto.TemaForoDTO;
import com.example.backend.dto.TemaForoUpdateDTO;
import com.example.backend.service.TemaForoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de temas en el foro.
 */
@RestController
@RequestMapping("/api/temas-foro")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Temas de Foro", description = "Operaciones para crear, consultar, modificar y eliminar temas del foro")
public class TemaForoController {

    private final TemaForoService temaService;

    @GetMapping
    @Operation(summary = "Listar temas", description = "Devuelve todos los temas del foro registrados")
    public List<TemaForoDTO> getAll() {
        return temaService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tema por ID", description = "Obtiene los datos de un tema específico mediante su ID")
    public TemaForoDTO getById(@PathVariable Long id) {
        return temaService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear tema", description = "Registra un nuevo tema en el foro")
    public TemaForoDTO create(@RequestBody TemaForoCreateDTO dto) {
        return temaService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tema", description = "Modifica los datos de un tema existente mediante su ID")
    public TemaForoDTO update(@PathVariable Long id, @RequestBody TemaForoUpdateDTO dto) {
        return temaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tema", description = "Elimina un tema del foro mediante su ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        temaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
