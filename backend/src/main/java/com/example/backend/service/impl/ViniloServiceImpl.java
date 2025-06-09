// Servicio que gestiona la lógica de negocio relacionada con los vinilos.
// Incluye operaciones CRUD, validaciones específicas y control de duplicados por proveedor.
package com.example.backend.service.impl;

import com.example.backend.dto.*;
import com.example.backend.model.*;
import com.example.backend.mapper.ViniloMapper;
import com.example.backend.repository.*;
import com.example.backend.service.ViniloService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor // Genera el constructor con todos los campos final inyectados automáticamente
public class ViniloServiceImpl implements ViniloService {

    private final ViniloRepository viniloRepository;
    private final ProveedorRepository proveedorRepository;
    private final ViniloMapper viniloMapper;

    /**
     * Recupera todos los vinilos del sistema y los transforma a DTOs.
     */
    @Override
    public List<ViniloDTO> findAll() {
        return viniloRepository.findAll()
                .stream()
                .map(viniloMapper::toDTO)
                .toList();
    }

    /**
     * Busca un vinilo por su ID. Lanza excepción si no existe.
     */
    @Override
    public ViniloDTO findById(Long id) {
        Vinilo v = viniloRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Vinilo no encontrado"
                ));
        return viniloMapper.toDTO(v);
    }

    /**
     * Crea un nuevo vinilo tras validar todos los campos requeridos y evitar duplicados por proveedor.
     */
    @Override
    public ViniloDTO create(ViniloCreateDTO dto) {
        // 1) Comprobar existencia del proveedor
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Proveedor no encontrado"
            ));

        // 2) Validar título (no vacío y no duplicado para ese proveedor)
        String titulo = dto.getTitulo().trim();
        if (titulo.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El título no puede estar vacío"
            );
        }
        if (viniloRepository.existsByTituloAndProveedorId(titulo, proveedor.getId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Ya existe un vinilo con ese título para este proveedor"
            );
        }

        // 3) Validar precio
        if (dto.getPrecio() == null || dto.getPrecio() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El precio debe ser un valor positivo"
            );
        }

        // 4) Validar stock
        if (dto.getStock() == null || dto.getStock() < 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El stock no puede ser negativo"
            );
        }

        // 5) Mapear DTO a entidad, asignar proveedor y guardar
        Vinilo v = viniloMapper.fromCreateDTO(dto);
        v.setTitulo(titulo);
        v.setProveedor(proveedor);

        Vinilo saved = viniloRepository.save(v);
        return viniloMapper.toDTO(saved);
    }

    /**
     * Actualiza un vinilo existente con datos provenientes de un DTO.
     * Valida cada campo si se incluye en el DTO, y gestiona cambios de proveedor y duplicados.
     */
    @Override
    public ViniloDTO update(Long id, ViniloUpdateDTO dto) {
        // 1) Buscar vinilo
        Vinilo v = viniloRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Vinilo no encontrado"
            ));

        // 2) Validar cambio de título o proveedor y evitar duplicados
        boolean cambioTitulo = dto.getTitulo() != null && !dto.getTitulo().trim().equals(v.getTitulo());
        boolean cambioProv = dto.getIdProveedor() != null && !dto.getIdProveedor().equals(v.getProveedor().getId());

        if (cambioTitulo || cambioProv) {
            String nuevoTitulo = cambioTitulo ? dto.getTitulo().trim() : v.getTitulo();
            Long nuevoProvId = cambioProv ? dto.getIdProveedor() : v.getProveedor().getId();

            if (viniloRepository.existsByTituloAndProveedorIdAndIdNot(nuevoTitulo, nuevoProvId, id)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe otro vinilo con ese título para este proveedor"
                );
            }

            v.setTitulo(nuevoTitulo);

            Proveedor proveedor = proveedorRepository.findById(nuevoProvId)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Proveedor no encontrado"
                ));
            v.setProveedor(proveedor);
        }

        // 3) Actualizar campos opcionales si están presentes en el DTO

        if (dto.getArtista() != null) {
            v.setArtista(dto.getArtista().trim());
        }

        if (dto.getGenero() != null) {
            v.setGenero(dto.getGenero().trim());
        }

        if (dto.getPrecio() != null) {
            if (dto.getPrecio() <= 0) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El precio debe ser un valor positivo"
                );
            }
            v.setPrecio(dto.getPrecio());
        }

        if (dto.getStock() != null) {
            if (dto.getStock() < 0) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El stock no puede ser negativo"
                );
            }
            v.setStock(dto.getStock());
        }

        if (dto.getImagen() != null) {
            v.setImagen(dto.getImagen().trim());
        }

        if (dto.getDescripcion() != null) {
            v.setDescripcion(dto.getDescripcion().trim());
        }

        // 4) Guardar cambios
        Vinilo updated = viniloRepository.save(v);
        return viniloMapper.toDTO(updated);
    }

    /**
     * Elimina un vinilo si existe. Si no, lanza excepción.
     */
    @Override
    public void delete(Long id) {
        if (!viniloRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Vinilo no existe"
            );
        }
        viniloRepository.deleteById(id);
    }
}
