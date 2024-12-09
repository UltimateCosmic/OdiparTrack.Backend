package com.odipartrack.controller;

import com.odipartrack.service.*;
import com.odipartrack.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
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
        calcularTiemposRutas(routes);

        List<Block> bloqueos = bloqueoService.obtenerBloqueos();
        List<Sale> sales = saleService.obtenerPedidosPorFecha(startDatetime);
        List<Camion> camiones = camionService.obtenerCamiones();
        List<Envio> envios = envioService.obtenerEnvios();

        // Asignar distancia y filtrar bloqueos por pedidos
        bloqueos = filtrarBloqueos(bloqueos, sales);

        // Planificación de envios (front)
        List<Envio> enviosPlanificacion = simulatedAnnealingService.getBestSolution(sales, routes, offices, velocidades,
                bloqueos, camiones, envios);

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

    private void leerDistanciasRutas(List<Route> rutas) {
        for (Route route : rutas) {
            Office start = route.getOrigin();
            Office end = route.getDestination();
            double distance = calculateDistance(start.getLatitude(), start.getLongitude(),
                    end.getLatitude(), end.getLongitude());
            route.setDistance(distance);
        }
    }

    // Método para calcular la distancia entre dos puntos geográficos usando la
    // fórmula de Haversine
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en kilómetros
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distancia en kilómetros
    }

    private void calcularTiemposRutas(List<Route> routes) {
        for (Route route : routes) {
            route.calculateTime();
        }
    }
}
