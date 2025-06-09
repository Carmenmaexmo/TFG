// Implementación del servicio de gestión de foros.
// Se encarga de operaciones CRUD sobre foros con validaciones de negocio.
package com.example.backend.service.impl;

import com.example.backend.dto.ForoCreateDTO;
import com.example.backend.dto.ForoDTO;
import com.example.backend.dto.ForoUpdateDTO;
import com.example.backend.model.Foro;
import com.example.backend.mapper.ForoMapper;
import com.example.backend.repository.ForoRepository;
import com.example.backend.service.ForoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service // Define esta clase como un servicio de Spring
@RequiredArgsConstructor // Genera constructor con los campos final para inyección de dependencias
public class ForoServiceImpl implements ForoService {

    private final ForoRepository foroRepo;
    private final ForoMapper foroMapper;

    /**
     * Devuelve una lista de todos los foros del sistema.
     */
    @Override
    public List<ForoDTO> findAll() {
        return foroRepo.findAll()
                .stream()
                .map(foroMapper::toDTO)
                .toList();
    }

    /**
     * Busca un foro por su ID. Lanza excepción si no existe.
     */
    @Override
    public ForoDTO findById(Long id) {
        Foro foro = foroRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Foro no encontrado"));
        return foroMapper.toDTO(foro);
    }

    /**
     * Crea un nuevo foro. Valida nombre único y descripción obligatoria.
     */
    @Override
    public ForoDTO create(ForoCreateDTO dto) {
        // 1) Validar nombre obligatorio y no duplicado
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El nombre del foro es obligatorio"
            );
        }
        String nombreTrim = dto.getNombre().trim();
        if (foroRepo.existsByNombre(nombreTrim)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Ya existe un foro con ese nombre"
            );
        }

        // 2) Validar descripción obligatoria
        if (dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La descripción es obligatoria"
            );
        }

        // 3) Crear y guardar el foro
        Foro foro = foroMapper.fromCreateDTO(dto);
        foro.setNombre(nombreTrim); // Normaliza el nombre sin espacios
        Foro guardado = foroRepo.save(foro);
        return foroMapper.toDTO(guardado);
    }

    /**
     * Actualiza los datos de un foro existente. Soporta actualizaciones parciales.
     */
    @Override
    public ForoDTO update(Long id, ForoUpdateDTO dto) {
        Foro foro = foroRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Foro no encontrado"
            ));

        // 1) Actualizar nombre si se proporciona
        if (dto.getNombre() != null) {
            String nombreTrim = dto.getNombre().trim();
            if (nombreTrim.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El nombre no puede estar vacío"
                );
            }
            // Verifica que no esté usando un nombre ya existente (distinto al suyo)
            if (!nombreTrim.equals(foro.getNombre()) && foroRepo.existsByNombre(nombreTrim)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ya existe un foro con ese nombre"
                );
            }
            foro.setNombre(nombreTrim);
        }

        // 2) Actualizar descripción si se proporciona
        if (dto.getDescripcion() != null) {
            String descTrim = dto.getDescripcion().trim();
            if (descTrim.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La descripción no puede estar vacía"
                );
            }
            foro.setDescripcion(descTrim);
        }

        // Guardar cambios
        Foro actualizado = foroRepo.save(foro);
        return foroMapper.toDTO(actualizado);
    }

    /**
     * Elimina un foro por su ID sin validaciones adicionales.
     */
    @Override
    public void delete(Long id) {
        foroRepo.deleteById(id);
    }
}
