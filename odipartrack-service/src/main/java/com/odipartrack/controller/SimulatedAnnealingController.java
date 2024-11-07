package com.odipartrack.controller;

import com.odipartrack.dto.*;
import com.odipartrack.model.*;
import com.odipartrack.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/api/simulated-annealing")
public class SimulatedAnnealingController {

    @Autowired
    private SimulatedAnnealingService simulatedAnnealingService;

    @PostMapping("/best-solution")
    public List<Envio> getBestSolution(@RequestBody SimulatedAnnealingRequest request) {
        return simulatedAnnealingService.getBestSolution(
            request.getSales(),
            request.getRoutes(),
            request.getOffices(),
            request.getVelocidades(),
            request.getBloqueos()
        );
    }
}
