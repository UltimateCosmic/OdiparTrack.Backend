package com.odipartrack.controller;

import com.odipartrack.service.*;
import com.odipartrack.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public List<Envio> getBestSolution(@RequestParam("fechaHora") String fechaHora) {
        // Parsear la fecha y hora del parámetro
        LocalDateTime startDatetime = LocalDateTime.parse(fechaHora);

        // Obtener los datos filtrados por fecha y hora
        List<Office> offices = officeService.obtenerOficinas();
        List<Velocidad> velocidades = velocidadService.obtenerVelocidades();
        List<Route> routes = routeService.obtenerRutas();
        List<Block> bloqueos = bloqueoService.obtenerBloqueos();
        List<Sale> sales = saleService.obtenerPedidosPorFecha(startDatetime);
        bloqueos = filtrarBloqueos(bloqueos, sales);
        List<Camion> camiones = camionService.obtenerCamiones();
        List<Envio> envios = envioService.obtenerEnvios();

        /*
         * -- *** ANTES DE EMPEZAR EJECUCIÓN
         * -- Asignar Distancia
         */

        // Planificación de envios (front)
        List<Envio> enviosPlanificacion = simulatedAnnealingService.getBestSolution(sales, routes, offices, velocidades,
                bloqueos, camiones, envios);

        /*
         * -- *** DESPUÉS DE C/PLANIFICACIÓN
         * -- Actualizar (CAMION): Tiempo_nuevo_envio
         * -- Insertar (ENVIO)
         * -- Actualizar (PEDIDOS): Envio + Camion
         * -- Actualizar (RUTAXSPEDIDOS): Pedido + Ruta
         */

        return enviosPlanificacion;
    }

    private List<Block> filtrarBloqueos(List<Block> bloqueos, List<Sale> sales) {

        LocalDateTime firstSaleDate = sales.get(0).getDateTime();
        LocalDateTime lastSaleDate = sales.get(sales.size() - 1).getDateTime();
        List<Block> bloqueosFiltrados = new ArrayList<>();

        for (Block bloqueo : bloqueos) {

            LocalDateTime startDateTime = bloqueo.getStart();
            LocalDateTime endDateTime = bloqueo.getEnd();

            if ((firstSaleDate.isAfter(startDateTime) && firstSaleDate.isBefore(endDateTime)) ||
                    (lastSaleDate.isAfter(startDateTime) && lastSaleDate.isBefore(endDateTime)) ||
                    (startDateTime.isAfter(firstSaleDate) && endDateTime.isBefore(lastSaleDate))) {
                bloqueosFiltrados.add(bloqueo);

            }            
        }
        return bloqueosFiltrados;
    }
}