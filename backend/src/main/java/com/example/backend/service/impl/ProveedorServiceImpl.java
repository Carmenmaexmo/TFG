package com.example.backend.service.impl;

import com.example.backend.dto.ProveedorDTO;
import com.example.backend.dto.ProveedorCreateDTO;
import com.example.backend.model.Proveedor;
import com.example.backend.mapper.ProveedorMapper;
import com.example.backend.repository.ProveedorRepository;
import com.example.backend.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Proveedor proveedor = proveedorMapper.fromCreateDTO(dto);
        return proveedorMapper.toDTO(proveedorRepository.save(proveedor));
    }

    @Override
    public ProveedorDTO update(Long id, ProveedorCreateDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        proveedor.setNombre(dto.getNombre());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());

        return proveedorMapper.toDTO(proveedorRepository.save(proveedor));
    }

    @Override
    public void delete(Long id) {
        proveedorRepository.deleteById(id);
    }
}
