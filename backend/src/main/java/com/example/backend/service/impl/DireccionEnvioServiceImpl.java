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

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DireccionEnvioServiceImpl implements DireccionEnvioService {

    private final DireccionEnvioRepository direccionEnvioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionEnvioMapper direccionEnvioMapper;

     // Expresiones regulares
    private static final String CP_REGEX   = "\\d{5}";
    private static final String TEL_REGEX = "^[6-9]\\d{8}$";

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
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Dirección no encontrada"
            ));
        return direccionEnvioMapper.toDTO(direccion);
    }

    @Override
    public DireccionEnvioDTO create(DireccionEnvioCreateDTO dto) {
        // 1) Validar usuario
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));

        // 2) Validar campos obligatorios
        if (dto.getDireccion() == null || dto.getDireccion().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La dirección es obligatoria"
            );
        }
        if (dto.getCiudad() == null || dto.getCiudad().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La ciudad es obligatoria"
            );
        }
        if (dto.getCodigoPostal() == null ||
            !dto.getCodigoPostal().matches(CP_REGEX)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Código postal inválido. Debe tener 5 dígitos"
            );
        }
        if (dto.getTelefono() == null
            || !dto.getTelefono().matches(TEL_REGEX)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Teléfono inválido. Debe tener 9 dígitos y empezar por 6,7,8 o 9"
            );
        }

        // 3) Crear y guardar
        DireccionEnvio direccion = direccionEnvioMapper.fromCreateDTO(dto);
        direccion.setUsuario(usuario);
        return direccionEnvioMapper.toDTO(
            direccionEnvioRepository.save(direccion)
        );
    }

    @Override
    public DireccionEnvioDTO update(Long id, DireccionEnvioCreateDTO dto) {
        DireccionEnvio dir = direccionEnvioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Dirección no encontrada"
            ));

        // 1) Ciudad
        if (dto.getCiudad() != null) {
            if (dto.getCiudad().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La ciudad no puede estar vacía"
                );
            }
            dir.setCiudad(dto.getCiudad());
        }

        // 2) Dirección
        if (dto.getDireccion() != null) {
            if (dto.getDireccion().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La dirección no puede estar vacía"
                );
            }
            dir.setDireccion(dto.getDireccion());
        }

        // 3) Código postal
        if (dto.getCodigoPostal() != null) {
            if (!dto.getCodigoPostal().matches(CP_REGEX)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Código postal inválido. Debe tener 5 dígitos"
                );
            }
            dir.setCodigoPostal(dto.getCodigoPostal());
        }

        // 4) Teléfono
        if (dto.getTelefono() != null) {
            if (!dto.getTelefono().matches(TEL_REGEX)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Teléfono inválido. Debe tener 9 dígitos y empezar por 6,7,8 o 9"
                );
            }
            dir.setTelefono(dto.getTelefono());
        }

        // 5) Usuario (relación) — si se permitiera cambiar, podrías añadir aquí:
        if (dto.getIdUsuario() != null &&
            !dto.getIdUsuario().equals(dir.getUsuario().getIdUsuario())) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
            dir.setUsuario(usuario);
        }

        // 6) Guardar cambios
        return direccionEnvioMapper.toDTO(
            direccionEnvioRepository.save(dir)
        );
    }

    @Override
    public void delete(Long id) {
        if (!direccionEnvioRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Dirección no existe"
            );
        }
        direccionEnvioRepository.deleteById(id);
    }
}
