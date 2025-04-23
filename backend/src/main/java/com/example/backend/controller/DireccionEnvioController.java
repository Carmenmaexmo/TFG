package com.example.backend.controller;

import com.example.backend.dto.DireccionEnvioDTO;
import com.example.backend.dto.DireccionEnvioCreateDTO;
import com.example.backend.service.DireccionEnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/direcciones-envio")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DireccionEnvioController {

    private final DireccionEnvioService direccionEnvioService;

    @GetMapping
    public List<DireccionEnvioDTO> getAll() {
        return direccionEnvioService.findAll();
    }

    @GetMapping("/{id}")
    public DireccionEnvioDTO getById(@PathVariable Long id) {
        return direccionEnvioService.findById(id);
    }

    @PostMapping
    public DireccionEnvioDTO create(@RequestBody DireccionEnvioCreateDTO dto) {
        return direccionEnvioService.create(dto);
    }

    @PutMapping("/{id}")
    public DireccionEnvioDTO update(@PathVariable Long id, @RequestBody DireccionEnvioCreateDTO dto) {
        return direccionEnvioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        direccionEnvioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
