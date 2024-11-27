package com.odipartrack.service;

import com.odipartrack.model.*;
import com.odipartrack.algorithm.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SimulatedAnnealingService {

    @Autowired
    private CamionService camionService;

    public List<Envio> getBestSolution(List<Sale> sales, List<Route> routes, List<Office> offices, List<Velocidad> velocidades, List<Block> bloqueos, List <Camion> camiones, List<Envio> envios) {        
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(sales, routes, 1000, 0.2, 1500, offices, velocidades, bloqueos, camiones);
        List<Envio> bestSolution = simulatedAnnealing.run();
        camionService.actualizarSalidaCamiones(bestSolution);
        return bestSolution;
    }
}
