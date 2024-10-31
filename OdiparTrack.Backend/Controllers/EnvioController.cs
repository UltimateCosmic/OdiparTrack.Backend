using Microsoft.AspNetCore.Mvc;
using OdiparTrack.Models;
using OdiparTrack.Services;

namespace OdiparTrack.Controllers
{
    [Route("api/envio")]
    [ApiController]
    public class EnvioController : ControllerBase
    {
        private readonly ILogger<EnvioController> _logger;
        private readonly EnvioService _envioService;

        public EnvioController(ILogger<EnvioController> logger, EnvioService envioService)
        {
            _logger = logger;
            _envioService = envioService;
        }

        [HttpPost("crear")]
        public async Task<IActionResult> CrearEnvio([FromBody] Envio envio)
        {
            try
            {
                // Llama al servicio para insertar el envío
                await _envioService.InsertarEnvio(envio.TiempoSalida.Value, envio.TiempoLlegada.Value, envio.CapacidadRestante.Value, envio.IdCamion.Value);
                return Ok(new { success = true, message = "Envio insertado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al insertar envío");
                return BadRequest(new { success = false, message = "Error al insertar el envío." });
            }
        }

        [HttpPut("actualizar")]
        public async Task<IActionResult> ActualizarEnvio([FromBody] Envio envio)
        {
            try
            {
                // Llama al servicio para actualizar el envío
                await _envioService.ActualizarEnvio(
                    envio.Id,
                    envio.TiempoSalida.Value,
                    envio.TiempoLlegada,
                    envio.CapacidadRestante,
                    envio.IdCamion.Value
                );

                return Ok(new { success = true, message = "Envio actualizado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al actualizar envío");
                return BadRequest(new { success = false, message = "Error al actualizar el envío." });
            }
        }

        [HttpDelete("eliminar/{id}")]
        public async Task<IActionResult> EliminarEnvio(int id)
        {
            try
            {
                await _envioService.EliminarEnvio(id);
                return Ok(new { success = true, message = "Envio eliminado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al eliminar envío");
                return BadRequest(new { success = false, message = "Error al eliminar el envío." });
            }
        }

        [HttpGet("leer")]
        public async Task<IActionResult> LeerEnvios()
        {
            try
            {
                List<Envio> envios = await _envioService.LeerEnvios();
                return Ok(new { success = true, data = envios });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al leer los envíos");
                return BadRequest(new { success = false, message = "Error al leer los envíos." });
            }
        }
    }
}
