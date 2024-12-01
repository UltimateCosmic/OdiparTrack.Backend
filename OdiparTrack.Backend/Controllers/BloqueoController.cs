using Microsoft.AspNetCore.Mvc;
using OdiparTrack.Models;
using OdiparTrack.Services;

namespace OdiparTrack.Controllers
{
    [Route("api/bloqueo")]
    [ApiController]
    public class BloqueoController : ControllerBase
    {
        private readonly ILogger<BloqueoController> _logger;
        private readonly BloqueoService _bloqueoService;

        public BloqueoController(ILogger<BloqueoController> logger, BloqueoService bloqueoService)
        {
            _logger = logger;
            _bloqueoService = bloqueoService;
        }

        [HttpPost("crear")]
        public async Task<IActionResult> CrearBloqueo([FromBody] Bloqueo bloqueo)
        {
            try
            {
                await _bloqueoService.InsertarBloqueo(
                    bloqueo.FechaInicio,
                    bloqueo.FechaFin,
                    bloqueo.IdRuta
                );

                return Ok(new { success = true, message = "Bloqueo insertado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al insertar bloqueo");
                return BadRequest(new { success = false, message = "Error al insertar el bloqueo." });
            }
        }

        [HttpPut("actualizar")]
        public async Task<IActionResult> ActualizarBloqueo([FromBody] Bloqueo bloqueo)
        {
            try
            {
                await _bloqueoService.ActualizarBloqueo(
                    bloqueo.Id,
                    bloqueo.FechaInicio,
                    bloqueo.FechaFin,
                    bloqueo.IdRuta
                );

                return Ok(new { success = true, message = "Bloqueo actualizado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al actualizar bloqueo");
                return BadRequest(new { success = false, message = "Error al actualizar el bloqueo." });
            }
        }

        [HttpDelete("eliminar/{id}")]
        public async Task<IActionResult> EliminarBloqueo(int id)
        {
            try
            {
                await _bloqueoService.EliminarBloqueo(id);
                return Ok(new { success = true, message = "Bloqueo eliminado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al eliminar bloqueo");
                return BadRequest(new { success = false, message = "Error al eliminar el bloqueo." });
            }
        }

        [HttpGet("leer")]
        public async Task<IActionResult> LeerBloqueos()
        {
            try
            {
                List<Bloqueo> bloqueos = await _bloqueoService.LeerBloqueos();
                return Ok(new { success = true, data = bloqueos });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al leer los bloqueos");
                return BadRequest(new { success = false, message = "Error al leer los bloqueos." });
            }
        }

        [HttpGet("leerPorFecha")]
        public async Task<IActionResult> LeerBloqueosPorFecha([FromQuery] DateTime startDate)
        {
            try
            {
                // Llama al servicio para obtener los bloqueos por la fecha proporcionada
                var bloqueos = await _bloqueoService.LeerBloqueosPorFechaAsync(startDate);

                // Devuelve la respuesta como un objeto JSON
                return Ok(new { success = true, data = bloqueos });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al leer los bloqueos por fecha");
                return BadRequest(new { success = false, message = "Error al leer los bloqueos por fecha." });
            }
        }
    }
}
