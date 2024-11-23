package com.odipartrack.controller;

import com.odipartrack.model.Velocidad;
import com.odipartrack.service.VelocidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/velocidades")
public class VelocidadController {

    @Autowired
    private VelocidadService velocidadService;

    /**
     * Endpoint para obtener todas las velocidades.
     *
     * @return Lista de velocidades.
     */
    @GetMapping("/leer")
    public ResponseEntity<List<Velocidad>> obtenerVelocidades() {
        List<Velocidad> velocidades = velocidadService.obtenerVelocidades();
        return ResponseEntity.ok(velocidades);
    }
}
