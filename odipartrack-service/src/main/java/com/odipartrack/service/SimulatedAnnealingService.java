package com.odipartrack.service;

import com.odipartrack.model.*;
import com.odipartrack.algorithm.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SimulatedAnnealingService {

    public List<Envio> getBestSolution() {
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();
        return simulatedAnnealing.run();
    }
}
