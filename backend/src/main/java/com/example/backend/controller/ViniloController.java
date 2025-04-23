package com.example.backend.controller;

import com.example.backend.dto.ViniloDTO;
import com.example.backend.dto.ViniloCreateDTO;
import com.example.backend.service.ViniloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vinilos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ViniloController {

    private final ViniloService viniloService;

    @GetMapping
    public List<ViniloDTO> getAll() {
        return viniloService.findAll();
    }

    @GetMapping("/{id}")
    public ViniloDTO getById(@PathVariable Long id) {
        return viniloService.findById(id);
    }

    @PostMapping
    public ViniloDTO create(@RequestBody ViniloCreateDTO dto) {
        return viniloService.create(dto);
    }

    @PutMapping("/{id}")
    public ViniloDTO update(@PathVariable Long id, @RequestBody ViniloCreateDTO dto) {
        return viniloService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        viniloService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
