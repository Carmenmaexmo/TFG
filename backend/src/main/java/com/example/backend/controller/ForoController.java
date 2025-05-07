// src/main/java/com/example/backend/controller/ForoController.java
package com.example.backend.controller;

import com.example.backend.dto.ForoCreateDTO;
import com.example.backend.dto.ForoDTO;
import com.example.backend.dto.ForoUpdateDTO;
import com.example.backend.service.ForoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ForoController {

    private final ForoService foroService;

    @GetMapping
    public List<ForoDTO> getAll() { return foroService.findAll(); }

    @GetMapping("/{id}")
    public ForoDTO getById(@PathVariable Long id) { return foroService.findById(id); }

    @PostMapping
    public ForoDTO create(@RequestBody ForoCreateDTO dto) { return foroService.create(dto); }

    @PutMapping("/{id}")
    public ForoDTO update(@PathVariable Long id, @RequestBody ForoUpdateDTO dto) {
        return foroService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
