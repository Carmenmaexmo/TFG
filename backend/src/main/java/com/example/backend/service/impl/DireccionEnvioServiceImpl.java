package com.example.backend.service.impl;

import com.example.backend.dto.DireccionEnvioDTO;
import com.example.backend.dto.DireccionEnvioCreateDTO;
import com.example.backend.model.DireccionEnvio;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.DireccionEnvioMapper;
import com.example.backend.repository.DireccionEnvioRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.DireccionEnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DireccionEnvioServiceImpl implements DireccionEnvioService {

    private final DireccionEnvioRepository direccionEnvioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionEnvioMapper direccionEnvioMapper;

    @Override
    public List<DireccionEnvioDTO> findAll() {
        return direccionEnvioRepository.findAll()
                .stream()
                .map(direccionEnvioMapper::toDTO)
                .toList();
    }

    @Override
    public DireccionEnvioDTO findById(Long id) {
        DireccionEnvio direccion = direccionEnvioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
        return direccionEnvioMapper.toDTO(direccion);
    }

    @Override
    public DireccionEnvioDTO create(DireccionEnvioCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        DireccionEnvio direccion = direccionEnvioMapper.fromCreateDTO(dto);
        direccion.setUsuario(usuario);

        return direccionEnvioMapper.toDTO(direccionEnvioRepository.save(direccion));
    }

    @Override
    public DireccionEnvioDTO update(Long id, DireccionEnvioCreateDTO dto) {
        DireccionEnvio direccion = direccionEnvioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        direccion.setCiudad(dto.getCiudad());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setDireccion(dto.getDireccion());
        direccion.setTelefono(dto.getTelefono());

        return direccionEnvioMapper.toDTO(direccionEnvioRepository.save(direccion));
    }

    @Override
    public void delete(Long id) {
        direccionEnvioRepository.deleteById(id);
    }
}
