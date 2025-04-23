package com.example.backend.controller;

import com.example.backend.dto.ProveedorDTO;
import com.example.backend.dto.ProveedorCreateDTO;
import com.example.backend.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    public List<ProveedorDTO> getAll() {
        return proveedorService.findAll();
    }

    @GetMapping("/{id}")
    public ProveedorDTO getById(@PathVariable Long id) {
        return proveedorService.findById(id);
    }

    @PostMapping
    public ProveedorDTO create(@RequestBody ProveedorCreateDTO dto) {
        return proveedorService.create(dto);
    }

    @PutMapping("/{id}")
    public ProveedorDTO update(@PathVariable Long id, @RequestBody ProveedorCreateDTO dto) {
        return proveedorService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        proveedorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
