package com.odipartrack.service;

import com.odipartrack.model.*;
import com.odipartrack.algorithm.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SimulatedAnnealingService {

    public List<Envio> getBestSolution(List<Sale> sales, List<Route> routes, List<Office> offices,
            List<Velocidad> velocidades, List<Block> bloqueos, List<Envio> preprocessedShipments) {
        if (preprocessedShipments == null) {
            preprocessedShipments = List.of();
        }
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(sales, routes, 1000, 0.03, 1500, offices,
                velocidades, bloqueos, preprocessedShipments);
        return simulatedAnnealing.run();
    }
}
