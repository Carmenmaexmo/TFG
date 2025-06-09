// Implementación del servicio de direcciones de envío asociadas a los usuarios.
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

@Service // Marca esta clase como un componente de servicio de Spring
@RequiredArgsConstructor // Inyección de dependencias mediante constructor generado automáticamente
public class DireccionEnvioServiceImpl implements DireccionEnvioService {

    private final DireccionEnvioRepository direccionEnvioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionEnvioMapper direccionEnvioMapper;

    // Expresiones regulares para validaciones
    private static final String CP_REGEX   = "\\d{5}";        // Código postal: exactamente 5 dígitos
    private static final String TEL_REGEX = "^[6-9]\\d{8}$";  // Teléfono: 9 dígitos empezando por 6-9

    /**
     * Obtiene todas las direcciones registradas en la base de datos.
     */
    @Override
    public List<DireccionEnvioDTO> findAll() {
        return direccionEnvioRepository.findAll()
                .stream()
                .map(direccionEnvioMapper::toDTO)
                .toList();
    }

    /**
     * Busca una dirección por ID. Lanza 404 si no existe.
     */
    @Override
    public DireccionEnvioDTO findById(Long id) {
        DireccionEnvio direccion = direccionEnvioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Dirección no encontrada"
            ));
        return direccionEnvioMapper.toDTO(direccion);
    }

    /**
     * Busca todas las direcciones asociadas a un usuario.
     */
    @Override
    public List<DireccionEnvioDTO> findByUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));
    
        return direccionEnvioRepository.findByUsuario(usuario)
                .stream()
                .map(direccionEnvioMapper::toDTO)
                .toList();
    }

    /**
     * Crea una nueva dirección de envío validando todos los campos.
     */
    @Override
    public DireccionEnvioDTO create(DireccionEnvioCreateDTO dto) {
        // Validar existencia del usuario
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));

        // Validar dirección
        if (dto.getDireccion() == null || dto.getDireccion().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La dirección es obligatoria"
            );
        }

        // Validar ciudad
        if (dto.getCiudad() == null || dto.getCiudad().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La ciudad es obligatoria"
            );
        }

        // Validar código postal
        if (dto.getCodigoPostal() == null || !dto.getCodigoPostal().matches(CP_REGEX)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Código postal inválido. Debe tener 5 dígitos"
            );
        }

        // Validar teléfono
        if (dto.getTelefono() == null || !dto.getTelefono().matches(TEL_REGEX)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Teléfono inválido. Debe tener 9 dígitos y empezar por 6,7,8 o 9"
            );
        }

        // Crear entidad y guardar
        DireccionEnvio direccion = direccionEnvioMapper.fromCreateDTO(dto);
        direccion.setUsuario(usuario);
        return direccionEnvioMapper.toDTO(
            direccionEnvioRepository.save(direccion)
        );
    }

    /**
     * Actualiza una dirección existente. Permite cambios parciales.
     */
    @Override
    public DireccionEnvioDTO update(Long id, DireccionEnvioCreateDTO dto) {
        DireccionEnvio dir = direccionEnvioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Dirección no encontrada"
            ));

        // Validar y actualizar ciudad
        if (dto.getCiudad() != null) {
            if (dto.getCiudad().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La ciudad no puede estar vacía"
                );
            }
            dir.setCiudad(dto.getCiudad());
        }

        // Validar y actualizar dirección
        if (dto.getDireccion() != null) {
            if (dto.getDireccion().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La dirección no puede estar vacía"
                );
            }
            dir.setDireccion(dto.getDireccion());
        }

        // Validar y actualizar código postal
        if (dto.getCodigoPostal() != null) {
            if (!dto.getCodigoPostal().matches(CP_REGEX)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Código postal inválido. Debe tener 5 dígitos"
                );
            }
            dir.setCodigoPostal(dto.getCodigoPostal());
        }

        // Validar y actualizar teléfono
        if (dto.getTelefono() != null) {
            if (!dto.getTelefono().matches(TEL_REGEX)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Teléfono inválido. Debe tener 9 dígitos y empezar por 6,7,8 o 9"
                );
            }
            dir.setTelefono(dto.getTelefono());
        }

        // Actualizar usuario (opcional, en caso de que se permita cambiar la relación)
        if (dto.getIdUsuario() != null &&
            !dto.getIdUsuario().equals(dir.getUsuario().getIdUsuario())) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
            dir.setUsuario(usuario);
        }

        // Guardar cambios
        return direccionEnvioMapper.toDTO(
            direccionEnvioRepository.save(dir)
        );
    }

    /**
     * Elimina una dirección por ID. Lanza 404 si no existe.
     */
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
