using Microsoft.AspNetCore.Mvc;
using OdiparTrack.Models;
using OdiparTrack.Services;

namespace OdiparTrack.Controllers
{
    [Route("api/reporte")]
    [ApiController]
    public class ReporteController : ControllerBase
    {
        private readonly ILogger<ReporteController> _logger;
        private readonly ReporteService _reporteService;

        public ReporteController(ILogger<ReporteController> logger, ReporteService reporteService)
        {
            _logger = logger;
            _reporteService = reporteService;
        }

        [HttpPost("crear")]
        public async Task<IActionResult> CrearReporte([FromBody] Reporte reporte)
        {
            try
            {
                await _reporteService.InsertarReporte(
                    reporte.FechaHoraInicioPlanificacion,
                    reporte.FechaHoraFinPlanificacion,
                    reporte.CantidadPedidosCompletados,
                    reporte.CantidadPedidosPendientes,
                    reporte.CantidadVehiculosUtilizados,
                    reporte.ArchivoPdf
                );

                return Ok(new { success = true, message = "Reporte insertado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al insertar el reporte");
                return BadRequest(new { success = false, message = "Error al insertar el reporte." });
            }
        }

        [HttpGet("leer")]
        public async Task<IActionResult> LeerReportes()
        {
            try
            {
                List<Reporte> reportes = await _reporteService.LeerReportes();
                return Ok(new { success = true, data = reportes });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al leer los reportes");
                return BadRequest(new { success = false, message = "Error al leer los reportes." });
            }
        }

        [HttpGet("obtener/{id}")]
        public async Task<IActionResult> ObtenerReportePorId(int id)
        {
            try
            {
                Reporte reporte = await _reporteService.ObtenerReportePorId(id);
                if (reporte == null)
                    return NotFound(new { success = false, message = "Reporte no encontrado." });

                return Ok(new { success = true, data = reporte });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al obtener el reporte");
                return BadRequest(new { success = false, message = "Error al obtener el reporte." });
            }
        }
    }
}
