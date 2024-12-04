using Microsoft.AspNetCore.Mvc;
using OdiparTrack.Models;
using OdiparTrack.Services;

namespace OdiparTrack.Controllers
{
    [Route("api/camion")]
    [ApiController]
    public class CamionController : ControllerBase
    {
        private readonly ILogger<CamionController> _logger;
        private readonly CamionService _camionService;

        public CamionController(ILogger<CamionController> logger, CamionService camionService)
        {
            _logger = logger;
            _camionService = camionService;
        }

        [HttpPost("crear")]
        public async Task<IActionResult> CrearCamion([FromBody] Camion camion)
        {
            try
            {
                await _camionService.InsertarCamion(
                    camion.Codigo,
                    camion.IdOrigen,
                    camion.Capacidad.Value,
                    camion.TiempoNuevoEnvio.Value
                );

                return Ok(new { success = true, message = "Camion insertado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al insertar camión");
                return BadRequest(new { success = false, message = "Error al insertar el camión." });
            }
        }

        [HttpPut("actualizar")]
        public async Task<IActionResult> ActualizarCamion([FromBody] Camion camion)
        {
            try
            {
                await _camionService.ActualizarCamion(
                    camion.Id,
                    camion.Codigo,
                    camion.IdOrigen,
                    camion.Capacidad.Value,
                    camion.TiempoNuevoEnvio
                );

                return Ok(new { success = true, message = "Camion actualizado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al actualizar camión");
                return BadRequest(new { success = false, message = "Error al actualizar el camión." });
            }
        }

        [HttpGet("leer")]
        public async Task<IActionResult> LeerTodosCamiones()
        {
            try
            {
                List<Camion> camiones = await _camionService.LeerTodosCamiones();
                return Ok(new { success = true, data = camiones });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al leer los camiones");
                return BadRequest(new { success = false, message = "Error al leer los camiones." });
            }
        }

        [HttpPost("reiniciar")]
        public async Task<IActionResult> ReiniciarCamiones()
        {
            try
            {
                await _camionService.ReiniciarCamiones();
                return Ok(new { success = true, message = "Camiones reiniciados satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al reiniciar camiones");
                return BadRequest(new { success = false, message = "Error al reiniciar camiones." });
            }
        }
    }
}
