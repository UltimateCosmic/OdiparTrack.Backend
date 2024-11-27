package com.odipartrack.controller;

import com.odipartrack.model.Block;
import com.odipartrack.service.BloqueoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bloqueos")
public class BloqueoController {

    @Autowired
    private BloqueoService bloqueoService;

    /**
     * Endpoint para obtener todos los bloqueos.
     *
     * @return Lista de bloqueos.
     */
    @GetMapping("/leerPorFecha")
    public ResponseEntity<List<Block>> obtenerBloqueosPorFecha(@RequestParam("fechaHoraInicio") String fechaHoraInicio) {
        LocalDateTime fechaInicio = LocalDateTime.parse(fechaHoraInicio);
        List<Block> bloqueos = bloqueoService.obtenerBloqueosPorFecha(fechaInicio);
        return ResponseEntity.ok(bloqueos);
    }
    
    /**
     * Endpoint para obtener todos los bloqueos.
     *
     * @return Lista de bloqueos.
     */
    @GetMapping("/leer")
    public ResponseEntity<List<Block>> obtenerBloqueos() {
        List<Block> bloqueos = bloqueoService.obtenerBloqueos();
        return ResponseEntity.ok(bloqueos);
    }
}
