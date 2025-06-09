// src/main/java/com/example/backend/controller/ForoController.java
package com.example.backend.controller;

import com.example.backend.dto.ForoCreateDTO;
import com.example.backend.dto.ForoDTO;
import com.example.backend.dto.ForoUpdateDTO;
import com.example.backend.service.ForoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de foros.
 */
@RestController
@RequestMapping("/api/foros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Foros", description = "Operaciones relacionadas con la gestión de foros temáticos")
public class ForoController {

    private final ForoService foroService;

    @GetMapping
    @Operation(summary = "Listar foros", description = "Devuelve todos los foros registrados")
    public List<ForoDTO> getAll() {
        return foroService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar foro por ID", description = "Obtiene los datos de un foro específico mediante su ID")
    public ForoDTO getById(@PathVariable Long id) {
        return foroService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear foro", description = "Registra un nuevo foro temático")
    public ForoDTO create(@RequestBody ForoCreateDTO dto) {
        return foroService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar foro", description = "Modifica los datos de un foro existente mediante su ID")
    public ForoDTO update(@PathVariable Long id, @RequestBody ForoUpdateDTO dto) {
        return foroService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar foro", description = "Elimina un foro existente mediante su ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
