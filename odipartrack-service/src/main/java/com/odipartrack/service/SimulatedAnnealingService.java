package com.odipartrack.service;

import com.odipartrack.algorithm.SimulatedAnnealing;
import com.odipartrack.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulatedAnnealingService {

    private static final double DEFAULT_TEMPERATURE = 1000;
    private static final double DEFAULT_COOLING_RATE = 0.03;
    private static final int DEFAULT_MAX_ITERATIONS = 1500;

    public List<Envio> getBestSolution(List<Sale> sales, List<Route> routes, List<Office> offices,
                                       List<Velocidad> velocidades, List<Block> bloqueos, List<Envio> preprocessedShipments) {
        // Validar parámetros esenciales
        if (sales == null || routes == null || offices == null) {
            throw new IllegalArgumentException("Sales, routes, and offices cannot be null.");
        }

        // Asegurar que preprocessedShipments no sea null
        preprocessedShipments = (preprocessedShipments != null) ? preprocessedShipments : List.of();

        // Crear instancia del algoritmo
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(
                sales,
                routes,
                DEFAULT_TEMPERATURE,
                DEFAULT_COOLING_RATE,
                DEFAULT_MAX_ITERATIONS,
                offices,
                velocidades,
                bloqueos,
                preprocessedShipments
        );

        // Ejecutar y retornar la mejor solución
        return simulatedAnnealing.run();
    }
}
