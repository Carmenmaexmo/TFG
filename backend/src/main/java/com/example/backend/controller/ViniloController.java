package com.example.backend.controller;

import com.example.backend.dto.ViniloDTO;
import com.example.backend.dto.ViniloUpdateDTO;
import com.example.backend.dto.ViniloCreateDTO;
import com.example.backend.service.ViniloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ViniloDTO update(@PathVariable Long id, @RequestBody ViniloUpdateDTO dto) {
        return viniloService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        viniloService.delete(id);
        Map<String, String> response = Map.of(
            "message", "Vinilo con id " + id + " eliminado correctamente"
        );
        return ResponseEntity.ok(response);
    }

}
