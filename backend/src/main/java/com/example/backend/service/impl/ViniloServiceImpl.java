package com.example.backend.service.impl;

import com.example.backend.dto.ViniloDTO;
import com.example.backend.dto.ViniloUpdateDTO;
import com.example.backend.dto.ViniloCreateDTO;
import com.example.backend.model.Proveedor;
import com.example.backend.model.Vinilo;
import com.example.backend.mapper.ViniloMapper;
import com.example.backend.repository.ProveedorRepository;
import com.example.backend.repository.ViniloRepository;
import com.example.backend.service.ViniloService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViniloServiceImpl implements ViniloService {

    private final ViniloRepository viniloRepository;
    private final ProveedorRepository proveedorRepository;
    private final ViniloMapper viniloMapper;

    @Override
    public List<ViniloDTO> findAll() {
        return viniloRepository.findAll()
                .stream()
                .map(viniloMapper::toDTO)
                .toList();
    }

    @Override
    public ViniloDTO findById(Long id) {
        Vinilo v = viniloRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Vinilo no encontrado"
                ));
        return viniloMapper.toDTO(v);
    }

    @Override
    public ViniloDTO create(ViniloCreateDTO dto) {
        // 1) Proveedor existe
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Proveedor no encontrado"
            ));

        // 2) Título válido y no duplicado
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

        // 3) Precio positivo
        if (dto.getPrecio() == null || dto.getPrecio() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El precio debe ser un valor positivo"
            );
        }

        // 4) Stock no negativo
        if (dto.getStock() == null || dto.getStock() < 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El stock no puede ser negativo"
            );
        }

        // 5) Mapea, asigna proveedor y guarda
        Vinilo v = viniloMapper.fromCreateDTO(dto);
        v.setTitulo(titulo);
        v.setProveedor(proveedor);

        Vinilo saved = viniloRepository.save(v);
        return viniloMapper.toDTO(saved);
    }

    @Override
    public ViniloDTO update(Long id, ViniloUpdateDTO dto) {
        // 1) Recupera el vinilo existente
        Vinilo v = viniloRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Vinilo no encontrado"
            ));

        // 2) Si cambian título o proveedor, chequea duplicado y asigna
        boolean cambioTitulo = dto.getTitulo() != null && !dto.getTitulo().trim().equals(v.getTitulo());
        boolean cambioProv   = dto.getIdProveedor() != null && !dto.getIdProveedor().equals(v.getProveedor().getId());
        if (cambioTitulo || cambioProv) {
            String nuevoTitulo = cambioTitulo ? dto.getTitulo().trim() : v.getTitulo();
            Long   nuevoProvId = cambioProv   ? dto.getIdProveedor()    : v.getProveedor().getId();

            if (viniloRepository.existsByTituloAndProveedorIdAndIdNot(nuevoTitulo, nuevoProvId, id)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe otro vinilo con ese título para este proveedor"
                );
            }

            // asigna título
            v.setTitulo(nuevoTitulo);
            // asigna proveedor
            Proveedor proveedor = proveedorRepository.findById(nuevoProvId)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Proveedor no encontrado"
                ));
            v.setProveedor(proveedor);
        }

        // 3) Artista
        if (dto.getArtista() != null) {
            v.setArtista(dto.getArtista().trim());
        }

        // 4) Género
        if (dto.getGenero() != null) {
            v.setGenero(dto.getGenero().trim());
        }

        // 5) Precio
        if (dto.getPrecio() != null) {
            if (dto.getPrecio() <= 0) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El precio debe ser un valor positivo"
                );
            }
            v.setPrecio(dto.getPrecio());
        }

        // 6) Stock
        if (dto.getStock() != null) {
            if (dto.getStock() < 0) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El stock no puede ser negativo"
                );
            }
            v.setStock(dto.getStock());
        }

        // 7) Imagen
        if (dto.getImagen() != null) {
            v.setImagen(dto.getImagen().trim());
        }

        // 8) Descripción
        if (dto.getDescripcion() != null) {
            v.setDescripcion(dto.getDescripcion().trim());
        }

        // 9) Guarda y devuelve
        Vinilo updated = viniloRepository.save(v);
        return viniloMapper.toDTO(updated);
    }

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
