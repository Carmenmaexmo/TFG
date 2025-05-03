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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Override
    public List<PedidoDTO> findAll() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toDTO)
                .toList();
    }

    @Override
    public PedidoDTO findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return pedidoMapper.toDTO(pedido);
    }

    @Override
    public PedidoDTO create(PedidoCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        DireccionEnvio direccion = direccionEnvioRepository.findById(dto.getIdDireccionEnvio())
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(direccion);
        pedido.setEstado(dto.getEstado());
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setTotal(dto.getTotal());

        List<DetallePedido> detalles = new ArrayList<>();

        for (DetallePedidoCreateDTO item : dto.getDetalles()) {
            Vinilo vinilo = viniloRepository.findById(item.getIdVinilo())
                    .orElseThrow(() -> new RuntimeException("Vinilo no encontrado"));

            DetallePedido detalle = new DetallePedido();
            detalle.setVinilo(vinilo);
            detalle.setCantidad(item.getCantidad());
            detalle.setPedido(pedido);
            detalles.add(detalle);
        }

        pedido.setDetalles(detalles);
        pedidoRepository.save(pedido);
        detallePedidoRepository.saveAll(detalles);

        return pedidoMapper.toDTO(pedido);
    }
    
   @Override
    public PedidoDTO update(Long id, PedidoUpdateDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // 1) Actualiza cabecera
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        DireccionEnvio direccion = direccionEnvioRepository.findById(dto.getIdDireccionEnvio())
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(direccion);
        pedido.setEstado(dto.getEstado());
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setTotal(dto.getTotal());

        // 2) Mapea existentes por id
        Map<Long, DetallePedido> existentes = pedido.getDetalles().stream()
                .collect(Collectors.toMap(DetallePedido::getId, Function.identity()));

        List<DetallePedido> fusionados = new ArrayList<>();

        // 3) Funde lista entrante
        for (DetallePedidoUpsertDTO d : dto.getDetalles()) {
            if (d.getId() != null && existentes.containsKey(d.getId())) {
                // actualiza existente
                DetallePedido vieja = existentes.get(d.getId());
                vieja.setCantidad(d.getCantidad());
                fusionados.add(vieja);
            } else {
                // crea nueva línea
                Vinilo vinilo = viniloRepository.findById(d.getIdVinilo())
                        .orElseThrow(() -> new RuntimeException("Vinilo no encontrado"));
                DetallePedido nueva = new DetallePedido();
                nueva.setPedido(pedido);
                nueva.setVinilo(vinilo);
                nueva.setCantidad(d.getCantidad());
                fusionados.add(nueva);
            }
        }

        // 4) Asigna y guarda
        pedido.setDetalles(fusionados);
        pedidoRepository.save(pedido);
        detallePedidoRepository.saveAll(fusionados);

        return pedidoMapper.toDTO(pedido);
    }


    @Override
    public void delete(Long id) {
        pedidoRepository.deleteById(id);
    }
}
