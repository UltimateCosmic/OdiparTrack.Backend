package com.odipartrack.controller;

import com.odipartrack.model.Route;
import com.odipartrack.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
public class RouteController {

    @Autowired
    private RouteService routeService;

    /**
     * Endpoint para obtener todas las rutas.
     *
     * @return Lista de rutas.
     */
    @GetMapping("/leer")
    public ResponseEntity<List<Route>> obtenerRutas() {
        List<Route> rutas = routeService.obtenerRutas();
        return ResponseEntity.ok(rutas);
    }
}
