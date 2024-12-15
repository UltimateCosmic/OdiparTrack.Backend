using Microsoft.AspNetCore.Mvc;
using OdiparTrack.Services;
using OdiparTrack.Models;
using System;
using System.Collections.Generic;
using System.Linq;

namespace OdiparTrack.Controllers
{
    [Route("api/simulated-annealing")]
    [ApiController]
    public class SimulatedAnnealingController : ControllerBase
    {
        private readonly SimulatedAnnealingService _simulatedAnnealingService;
        private readonly SaleService _saleService;
        private readonly RouteService _routeService;
        private readonly OfficeService _officeService;
        private readonly VelocidadService _velocidadService;
        private readonly BloqueoService _bloqueoService;
        private readonly CamionService _camionService;
        private readonly EnvioService _envioService;

        public SimulatedAnnealingController(
            SimulatedAnnealingService simulatedAnnealingService,
            SaleService saleService,
            RouteService routeService,
            OfficeService officeService,
            VelocidadService velocidadService,
            BloqueoService bloqueoService,
            CamionService camionService,
            EnvioService envioService)
        {
            _simulatedAnnealingService = simulatedAnnealingService;
            _saleService = saleService;
            _routeService = routeService;
            _officeService = officeService;
            _velocidadService = velocidadService;
            _bloqueoService = bloqueoService;
            _camionService = camionService;
            _envioService = envioService;
        }

        [HttpPost("best-solution")]
        public ActionResult<List<Envio>> GetBestSolution([FromQuery] string fechaHora)
        {
            try
            {
                // Parsear la fecha y hora del parámetro
                var startDatetime = DateTime.Parse(fechaHora);

                // Obtener los datos necesarios para el procesamiento
                var offices = _officeService.ObtenerOficinas();
                var velocidades = _velocidadService.ObtenerVelocidades();
                var routes = _routeService.ObtenerRutas();
                var bloqueos = _bloqueoService.ObtenerBloqueos();
                var sales = _saleService.ObtenerPedidosPorFecha(startDatetime);
                var camiones = _camionService.ObtenerCamiones();
                var envios = _envioService.ObtenerEnvios();

                // Asignar distancia y filtrar bloqueos por pedidos
                LeerDistanciasRutas(routes);
                bloqueos = FiltrarBloqueos(bloqueos, sales);

                // Obtener la mejor solución utilizando el servicio de Simulated Annealing
                var enviosPlanificacion = _simulatedAnnealingService.GetBestSolution(
                    sales, routes, offices, velocidades, bloqueos, camiones, envios
                );

                return Ok(enviosPlanificacion);
            }
            catch (Exception ex)
            {
                // Loguear el error y devolver un mensaje de error
                Console.Error.WriteLine($"Error en GetBestSolution: {ex.Message}");
                return StatusCode(500, "Ocurrió un error en el procesamiento de la planificación.");
            }
        }

        private List<Block> FiltrarBloqueos(List<Block> bloqueos, List<Sale> sales)
        {
            var firstSaleDate = sales.First().DateTime;
            var lastSaleDate = sales.Last().DateTime;
            var bloqueosFiltrados = new List<Block>();

            foreach (var bloqueo in bloqueos)
            {
                var startDateTime = bloqueo.Start;
                var endDateTime = bloqueo.End;

                if ((firstSaleDate > startDateTime && firstSaleDate < endDateTime) ||
                    (lastSaleDate > startDateTime && lastSaleDate < endDateTime) ||
                    (startDateTime > firstSaleDate && endDateTime < lastSaleDate))
                {
                    bloqueosFiltrados.Add(bloqueo);
                }
            }
            return bloqueosFiltrados;
        }

        private void LeerDistanciasRutas(List<Route> rutas)
        {
            foreach (var route in rutas)
            {
                var start = route.Origin;
                var end = route.Destination;
                var distance = CalcularDistancia(start.Latitude, start.Longitude, end.Latitude, end.Longitude);
                route.Distance = distance;
            }
        }

        // Método para calcular la distancia entre dos puntos geográficos usando la fórmula de Haversine
        private static double CalcularDistancia(double lat1, double lon1, double lat2, double lon2)
        {
            const int R = 6371; // Radio de la Tierra en kilómetros
            var latDistance = Math.PI / 180 * (lat2 - lat1);
            var lonDistance = Math.PI / 180 * (lon2 - lon1);
            var a = Math.Sin(latDistance / 2) * Math.Sin(latDistance / 2) +
                    Math.Cos(Math.PI / 180 * lat1) * Math.Cos(Math.PI / 180 * lat2) *
                    Math.Sin(lonDistance / 2) * Math.Sin(lonDistance / 2);
            var c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));
            return R * c; // Distancia en kilómetros
        }
    }
}

