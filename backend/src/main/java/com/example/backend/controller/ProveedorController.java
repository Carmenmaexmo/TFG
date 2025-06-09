package com.example.backend.controller;

import com.example.backend.dto.ProveedorDTO;
import com.example.backend.dto.ProveedorUpdateDTO;
import com.example.backend.dto.ProveedorCreateDTO;
import com.example.backend.service.ProveedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de proveedores.
 */
@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Proveedores", description = "Operaciones para consultar, registrar, actualizar o eliminar proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    @Operation(summary = "Listar proveedores", description = "Devuelve todos los proveedores registrados en el sistema")
    public List<ProveedorDTO> getAll() {
        return proveedorService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar proveedor por ID", description = "Obtiene los datos de un proveedor específico mediante su ID")
    public ProveedorDTO getById(@PathVariable Long id) {
        return proveedorService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear proveedor", description = "Registra un nuevo proveedor")
    public ProveedorDTO create(@RequestBody ProveedorCreateDTO dto) {
        return proveedorService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proveedor", description = "Modifica los datos de un proveedor existente mediante su ID")
    public ProveedorDTO update(
        @PathVariable Long id,
        @RequestBody ProveedorUpdateDTO dto
    ) {
        return proveedorService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor mediante su ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        proveedorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
