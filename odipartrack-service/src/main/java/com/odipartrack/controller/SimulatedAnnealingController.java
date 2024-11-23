package com.odipartrack.controller;

import com.odipartrack.service.*;
import com.odipartrack.util.DataFiller;
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
        List<Block> bloqueos = bloqueoService.obtenerBloqueos(); // Route
        List<Route> routes = routeService.obtenerRutas(); // Velocidad + Office
        List<Sale> sales = saleService.obtenerPedidos(); // Camion + Office + Envio
        List<Envio> envios = envioService.obtenerEnvios(); // Camion
        List<Camion> camiones = camionService.obtenerCamiones(); // Al final (complicado xd)

        // Llenado de datos
        DataFiller.fillRouteData(routes, offices, velocidades);
        DataFiller.fillBlockData(bloqueos, routes);
        DataFiller.fillSaleData(sales, offices, envios, camiones); // Camiones asignados parcialmente
        DataFiller.fillCamionData(camiones, offices, sales); // Pedidos asignados
        DataFiller.fillCamionRoutes(camiones, routes); // Rutas asignadas
        DataFiller.fillEnvioData(envios, camiones); // Camiones asignados a envíos

        // Ahora asigna camiones completos
        DataFiller.completeSaleData(sales, camiones);

        return simulatedAnnealingService.getBestSolution(sales, routes, offices, velocidades, bloqueos, camiones, envios);
    }
}
