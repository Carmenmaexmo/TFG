// Implementación del servicio de gestión de proveedores.
// Incluye validaciones de unicidad, formato y operaciones CRUD.
package com.example.backend.service.impl;

import com.example.backend.dto.*;
import com.example.backend.model.Proveedor;
import com.example.backend.mapper.ProveedorMapper;
import com.example.backend.repository.ProveedorRepository;
import com.example.backend.service.ProveedorService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service // Marca esta clase como servicio gestionado por Spring
@RequiredArgsConstructor // Lombok genera constructor con los campos final para inyección automática
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    /**
     * Devuelve todos los proveedores registrados.
     */
    @Override
    public List<ProveedorDTO> findAll() {
        return proveedorRepository.findAll()
                .stream()
                .map(proveedorMapper::toDTO)
                .toList();
    }

    /**
     * Busca un proveedor por su ID. Lanza excepción si no lo encuentra.
     */
    @Override
    public ProveedorDTO findById(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        return proveedorMapper.toDTO(proveedor);
    }

    /**
     * Crea un nuevo proveedor, validando nombre único, email y teléfono.
     */
    @Override
    public ProveedorDTO create(ProveedorCreateDTO dto) {
        String nombre   = dto.getNombre().trim();
        String email    = dto.getEmail().trim();
        String telefono = dto.getTelefono().trim();

        // 1) Validación de unicidad del nombre
        if (proveedorRepository.existsByNombre(nombre)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Ya existe un proveedor con nombre '" + nombre + "'"
            );
        }

        // 2) Validación de email
        if (!validarEmail(email)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Email inválido"
            );
        }
        if (proveedorRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Ya existe un proveedor con email '" + email + "'"
            );
        }

        // 3) Validación de teléfono
        if (!validarTelefono(telefono)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Teléfono inválido"
            );
        }

        // 4) Crear entidad y persistir
        Proveedor proveedor = proveedorMapper.fromCreateDTO(dto);
        proveedor.setNombre(nombre);
        proveedor.setEmail(email);
        proveedor.setTelefono(telefono);

        Proveedor saved = proveedorRepository.save(proveedor);
        return proveedorMapper.toDTO(saved);
    }

    /** Valida que el email tenga un formato simple correcto */
    private boolean validarEmail(String email) {
        if (email == null) return false;
        return email.trim().matches("^[\\w.+\\-]+@[\\w\\-]+\\.[A-Za-z]{2,}$");
    }

    /** Valida que el teléfono tenga 9 dígitos y empiece por 6-9 (formato español) */
    private boolean validarTelefono(String t) {
        if (t == null) return false;
        return t.trim().matches("^[6789]\\d{8}$");
    }

    /**
     * Actualiza los datos de un proveedor. Soporta actualizaciones parciales.
     * Valida que el nuevo nombre o email no estén repetidos en otro proveedor.
     */
    @Override
    public ProveedorDTO update(Long id, ProveedorUpdateDTO dto) {
        Proveedor p = proveedorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Proveedor no encontrado"
            ));

        // 1) Actualizar nombre si cambia
        if (dto.getNombre() != null && !dto.getNombre().equals(p.getNombre())) {
            String nombre = dto.getNombre().trim();
            if (proveedorRepository.existsByNombreAndIdNot(nombre, id)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe otro proveedor con nombre: " + nombre
                );
            }
            p.setNombre(nombre);
        }

        // 2) Actualizar email si cambia
        if (dto.getEmail() != null && !dto.getEmail().equals(p.getEmail())) {
            if (!validarEmail(dto.getEmail())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email inválido"
                );
            }
            if (proveedorRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email ya registrado"
                );
            }
            p.setEmail(dto.getEmail());
        }

        // 3) Actualizar teléfono
        if (dto.getTelefono() != null) {
            if (!validarTelefono(dto.getTelefono())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Teléfono inválido"
                );
            }
            p.setTelefono(dto.getTelefono());
        }

        // 4) Actualizar dirección si viene
        if (dto.getDireccion() != null) {
            p.setDireccion(dto.getDireccion());
        }

        return proveedorMapper.toDTO(proveedorRepository.save(p));
    }

    /**
     * Elimina un proveedor por ID sin validación adicional.
     */
    @Override
    public void delete(Long id) {
        proveedorRepository.deleteById(id);
    }
}
