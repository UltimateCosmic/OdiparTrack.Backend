package com.odipartrack.controller;

import com.odipartrack.model.*;
import com.odipartrack.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/simulated-annealing")
public class SimulatedAnnealingController {

    @Autowired
    private SimulatedAnnealingService simulatedAnnealingService;

    @GetMapping("/best-solution")
    public List<Envio> getBestSolution(List<Sale> sales, List<Route> routes, List<Office> offices, List<Velocidad> velocidades, List<Block> bloqueos) {
        return simulatedAnnealingService.getBestSolution(sales, routes, offices, velocidades, bloqueos);
    }
}
