package com.odipartrack.controller;

import com.odipartrack.service.*;
import com.odipartrack.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RestController
@RequestMapping("/api/simulated-annealing")
public class SimulatedAnnealingController {

    @Autowired
    private SimulatedAnnealingService simulatedAnnealingService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private VelocidadService velocidadService;

    @Autowired
    private BloqueoService bloqueoService;

    @Autowired
    private CamionService camionService;

    @Autowired
    private EnvioService envioService;

    @PostMapping("/best-solution")
    public List<Envio> getBestSolution() {

        // Lectura de los datos de la Base de Datos
        List<Office> offices = officeService.obtenerOficinas();
        List<Velocidad> velocidades = velocidadService.obtenerVelocidades();        
        List<Route> routes = routeService.obtenerRutas();
        List<Block> bloqueos = bloqueoService.obtenerBloqueos();
        List<Sale> sales = saleService.obtenerPedidos();
        List<Camion> camiones = camionService.obtenerCamiones();
        List<Envio> envios = envioService.obtenerEnvios();
        
        return simulatedAnnealingService.getBestSolution(sales, routes, offices, velocidades, bloqueos, camiones, envios);
    }
}
