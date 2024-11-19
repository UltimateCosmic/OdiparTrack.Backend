package com.odipartrack.controller;

import com.odipartrack.dto.SimulatedAnnealingRequest;
import com.odipartrack.model.Envio;
import com.odipartrack.service.SimulatedAnnealingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulated-annealing")
public class SimulatedAnnealingController {

    @Autowired
    private SimulatedAnnealingService simulatedAnnealingService;

    @PostMapping("/best-solution")
    public ResponseEntity<List<Envio>> getBestSolution(@RequestBody SimulatedAnnealingRequest request) {
        if (request.getSales() == null || request.getRoutes() == null || request.getOffices() == null) {
            return ResponseEntity.badRequest().build(); // Verificar parámetros esenciales
        }

        List<Envio> preprocessedShipments = (request.getEnvios() != null) ? request.getEnvios() : List.of();
        List<Envio> bestSolution = simulatedAnnealingService.getBestSolution(
                request.getSales(),
                request.getRoutes(),
                request.getOffices(),
                request.getVelocidades(),
                request.getBloqueos(),
                preprocessedShipments
        );

        return ResponseEntity.ok(bestSolution);
    }
}
