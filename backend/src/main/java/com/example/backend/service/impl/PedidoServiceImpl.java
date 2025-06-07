package com.example.backend.service.impl;

import com.example.backend.dto.PedidoCreateDTO;
import com.example.backend.dto.PedidoDTO;
import com.example.backend.dto.PedidoUpdateDTO;
import com.example.backend.dto.DetallePedidoCreateDTO;
import com.example.backend.dto.DetallePedidoUpsertDTO;
import com.example.backend.model.*;
import com.example.backend.mapper.PedidoMapper;
import com.example.backend.repository.*;
import com.example.backend.service.PedidoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionEnvioRepository direccionEnvioRepository;
    private final ViniloRepository viniloRepository;
    private final PedidoMapper pedidoMapper;
    private final DetallePedidoRepository detallePedidoRepository;

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
        "PENDIENTE", "ENVIADO", "ENTREGADO", "CANCELADO"
    );

    @Override
    public PedidoDTO create(PedidoCreateDTO dto) {
        // 1) Validación del estado
        if (dto.getEstado() == null || dto.getEstado().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El estado del pedido es obligatorio"
            );
        }
        String estadoNorm = dto.getEstado().trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(estadoNorm)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Estado inválido. Debe ser uno de " + ESTADOS_VALIDOS
            );
        }

        // 2) Validación de fecha y total
        if (dto.getFechaPedido() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La fecha de pedido es obligatoria"
            );
        }
        if (dto.getTotal() == null || dto.getTotal() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El total debe ser un número mayor que 0"
            );
        }

        // 3) Validación de referencias
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));
        DireccionEnvio direccion = direccionEnvioRepository.findById(dto.getIdDireccionEnvio())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Dirección de envío no encontrada"
            ));

        // 4) Crear y guardar cabecera
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(direccion);
        pedido.setEstado(estadoNorm);
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setTotal(dto.getTotal());
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 5) Crear y guardar detalles
        List<DetallePedido> detalles = new ArrayList<>();
        for (DetallePedidoCreateDTO item : dto.getDetalles()) {
            if (item.getCantidad() == null || item.getCantidad() < 1) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cada detalle debe tener cantidad ≥ 1"
                );
            }
            Vinilo vinilo = viniloRepository.findById(item.getIdVinilo())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Vinilo con id " + item.getIdVinilo() + " no encontrado"
                ));

            DetallePedido det = new DetallePedido();
            det.setPedido(pedidoGuardado);
            det.setVinilo(vinilo);
            det.setPrecio(vinilo.getPrecio());
            det.setCantidad(item.getCantidad());
            detalles.add(det);
        }
        detallePedidoRepository.saveAll(detalles);

        pedidoGuardado.setDetalles(detalles);
        return pedidoMapper.toDTO(pedidoGuardado);
    }

    @Override
    public PedidoDTO update(Long id, PedidoUpdateDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Pedido no encontrado"
            ));

        // 1) estado
        if (dto.getEstado() != null) {
        String estadoNorm = dto.getEstado().trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(estadoNorm)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Estado inválido. Debe ser uno de " + ESTADOS_VALIDOS
            );
        }

        // Verificamos si el estado nuevo es ENVIADO o ENTREGADO y el anterior no lo era
        boolean actualizarStock = 
            (estadoNorm.equals("ENVIADO") || estadoNorm.equals("ENTREGADO")) &&
            !(pedido.getEstado().equals("ENVIADO") || pedido.getEstado().equals("ENTREGADO"));

        pedido.setEstado(estadoNorm);

        // Restar stock SOLO si cambia a ENVIADO/ENTREGADO
        if (actualizarStock) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                Vinilo vinilo = detalle.getVinilo();
                int nuevaCantidad = vinilo.getStock() - detalle.getCantidad();
                vinilo.setStock(Math.max(nuevaCantidad, 0));
                viniloRepository.save(vinilo);
            }
        }
        }

        // 2) fechaPedido
        if (dto.getFechaPedido() != null) {
            pedido.setFechaPedido(dto.getFechaPedido());
        }

        // 3) total
        if (dto.getTotal() != null) {
            if (dto.getTotal() <= 0) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El total debe ser un número mayor que 0"
                );
            }
            pedido.setTotal(dto.getTotal());
        }

        // 4) usuario (relación)
        if (dto.getIdUsuario() != null
            && !dto.getIdUsuario().equals(pedido.getUsuario().getIdUsuario())) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
            pedido.setUsuario(usuario);
        }

        // 5) dirección de envío (relación)
        if (dto.getIdDireccionEnvio() != null
            && !dto.getIdDireccionEnvio().equals(pedido.getDireccionEnvio().getId())) {
            DireccionEnvio direccion = direccionEnvioRepository.findById(dto.getIdDireccionEnvio())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Dirección de envío no encontrada"
                ));
            pedido.setDireccionEnvio(direccion);
        }

        // 6) detalles sólo si vienen
        if (dto.getDetalles() != null) {
            Map<Long, DetallePedido> existentes = pedido.getDetalles().stream()
                .collect(Collectors.toMap(DetallePedido::getId, Function.identity()));
            List<DetallePedido> fusionados = new ArrayList<>();

            for (DetallePedidoUpsertDTO d : dto.getDetalles()) {
                if (d.getId() != null && existentes.containsKey(d.getId())) {
                    DetallePedido vieja = existentes.get(d.getId());
                    if (d.getCantidad() < 1) {
                        throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Cantidad de detalle debe ser ≥ 1"
                        );
                    }
                    vieja.setCantidad(d.getCantidad());
                    fusionados.add(vieja);
                } else {
                    Vinilo vinilo = viniloRepository.findById(d.getIdVinilo())
                        .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Vinilo con id " + d.getIdVinilo() + " no encontrado"
                        ));
                    DetallePedido nueva = new DetallePedido();
                    nueva.setPedido(pedido);
                    nueva.setVinilo(vinilo);
                    if (d.getCantidad() < 1) {
                        throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Cantidad de detalle debe ser ≥ 1"
                        );
                    }
                    nueva.setCantidad(d.getCantidad());
                    fusionados.add(nueva);
                }
            }

            pedido.setDetalles(fusionados);
            detallePedidoRepository.saveAll(fusionados);
        }

        // 7) guardar cambios en la cabecera
        pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedido);
    }

    @Override
    public void delete(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Pedido no existe"
            );
        }
        pedidoRepository.deleteById(id);
    }

    @Override
    public List<PedidoDTO> findAll() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toDTO)
                .toList();
    }

    @Override
    public PedidoDTO findById(Long id) {
        Pedido p = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Pedido no encontrado"
                ));
        return pedidoMapper.toDTO(p);
    }


    @Override
    public List<PedidoDTO> findByUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    
        return pedidoRepository.findByUsuario(usuario).stream()
            .map(pedidoMapper::toDTO)
            .collect(Collectors.toList());
    }    
    
}
