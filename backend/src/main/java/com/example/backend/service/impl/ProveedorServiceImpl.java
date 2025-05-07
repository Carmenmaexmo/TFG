package com.example.backend.service.impl;

import com.example.backend.dto.ProveedorDTO;
import com.example.backend.dto.ProveedorUpdateDTO;
import com.example.backend.dto.ProveedorCreateDTO;
import com.example.backend.model.Proveedor;
import com.example.backend.mapper.ProveedorMapper;
import com.example.backend.repository.ProveedorRepository;
import com.example.backend.service.ProveedorService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    @Override
    public List<ProveedorDTO> findAll() {
        return proveedorRepository.findAll()
                .stream()
                .map(proveedorMapper::toDTO)
                .toList();
    }

    @Override
    public ProveedorDTO findById(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        return proveedorMapper.toDTO(proveedor);
    }

     @Override
    public ProveedorDTO create(ProveedorCreateDTO dto) {
        String nombre   = dto.getNombre().trim();
        String email    = dto.getEmail().trim();
        String telefono = dto.getTelefono().trim();

        // 1) Nombre único
        if (proveedorRepository.existsByNombre(nombre)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe un proveedor con nombre '" + nombre + "'"
            );
        }
        // 2) Email válido y único
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
        // 3) Teléfono válido
        if (!validarTelefono(telefono)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Teléfono inválido"
            );
        }

        // Map y guardado
        Proveedor proveedor = proveedorMapper.fromCreateDTO(dto);
        proveedor.setNombre(nombre);
        proveedor.setEmail(email);
        proveedor.setTelefono(telefono);

        Proveedor saved = proveedorRepository.save(proveedor);
        return proveedorMapper.toDTO(saved);
    }

     /** Muy simple: algo@algo.algo */
     private boolean validarEmail(String email) {
        if (email == null) return false;
        return email.trim().matches("^[\\w.+\\-]+@[\\w\\-]+\\.[A-Za-z]{2,}$");
    }

    /** Para España: 9 dígitos, empieza en 6,7,8 o 9 */
    private boolean validarTelefono(String t) {
        if (t == null) return false;
        return t.trim().matches("^[6789]\\d{8}$");
    }

    @Override
    public ProveedorDTO update(Long id, ProveedorUpdateDTO dto) {
        Proveedor p = proveedorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Proveedor no encontrado"
            ));

        // 1) nombre único
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

        // 2) email
        if (dto.getEmail() != null && !dto.getEmail().equals(p.getEmail())) {
            if (!validarEmail(dto.getEmail())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email inválido"
                );
            }
            if (proveedorRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email ya registrado"
                );
            }
            p.setEmail(dto.getEmail());
        }

        // 3) teléfono
        if (dto.getTelefono() != null) {
            if (!validarTelefono(dto.getTelefono())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Teléfono inválido"
                );
            }
            p.setTelefono(dto.getTelefono());
        }

        // 4) dirección
        if (dto.getDireccion() != null) {
            p.setDireccion(dto.getDireccion());
        }

        return proveedorMapper.toDTO(proveedorRepository.save(p));
    }


    @Override
    public void delete(Long id) {
        proveedorRepository.deleteById(id);
    }
}
