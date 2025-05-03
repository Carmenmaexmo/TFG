package com.example.backend.service.impl;

import com.example.backend.dto.ViniloDTO;
import com.example.backend.dto.ViniloCreateDTO;
import com.example.backend.model.Proveedor;
import com.example.backend.model.Vinilo;
import com.example.backend.mapper.ViniloMapper;
import com.example.backend.repository.ProveedorRepository;
import com.example.backend.repository.ViniloRepository;
import com.example.backend.service.ViniloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Vinilo vinilo = viniloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vinilo no encontrado"));
        return viniloMapper.toDTO(vinilo);
    }

    @Override
    public ViniloDTO create(ViniloCreateDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Vinilo vinilo = viniloMapper.fromCreateDTO(dto);
        vinilo.setProveedor(proveedor);

        return viniloMapper.toDTO(viniloRepository.save(vinilo));
    }

    @Override
    public ViniloDTO update(Long id, ViniloCreateDTO dto) {
        Vinilo vinilo = viniloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vinilo no encontrado"));

        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        vinilo.setTitulo(dto.getTitulo());
        vinilo.setArtista(dto.getArtista());
        vinilo.setGenero(dto.getGenero());
        vinilo.setPrecio(dto.getPrecio());
        vinilo.setStock(dto.getStock());
        vinilo.setImagen(dto.getImagen());
        vinilo.setDescripcion(dto.getDescripcion());
        vinilo.setProveedor(proveedor);

        return viniloMapper.toDTO(viniloRepository.save(vinilo));
    }

    @Override
    public void delete(Long id) {
        viniloRepository.deleteById(id);
    }
}
