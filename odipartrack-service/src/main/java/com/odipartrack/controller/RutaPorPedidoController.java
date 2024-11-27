package com.odipartrack.controller;

import com.odipartrack.model.RutaPorPedido;
import com.odipartrack.service.RutaPorPedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rutas-por-pedidos")
public class RutaPorPedidoController {

    @Autowired
    private RutaPorPedidoService rutaPorPedidoService;

    /**
     * Endpoint para obtener todas las relaciones de Rutas por Pedidos.
     *
     * @return Lista de objetos RutaPorPedido.
     */
    @GetMapping
    public List<RutaPorPedido> obtenerRutasPorPedidos() {
        return rutaPorPedidoService.obtenerRutasPorPedidos();
    }
}
