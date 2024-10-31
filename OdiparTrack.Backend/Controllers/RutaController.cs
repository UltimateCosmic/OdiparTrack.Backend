using Microsoft.AspNetCore.Mvc;
using OdiparTrack.Models;
using OdiparTrack.Services;

namespace OdiparTrack.Controllers
{
    [Route("api/ruta")]
    [ApiController]
    public class RutaController : ControllerBase
    {
        private readonly ILogger<RutaController> _logger;
        private readonly RutaService _rutaService;

        public RutaController(ILogger<RutaController> logger, RutaService rutaService)
        {
            _logger = logger;
            _rutaService = rutaService;
        }

        [HttpPost("crear")]
        public async Task<IActionResult> CrearRuta([FromBody] Ruta ruta)
        {
            try
            {
                await _rutaService.InsertarRuta(
                    ruta.IdOrigen,
                    ruta.IdDestino,
                    ruta.Distancia.Value
                );

                return Ok(new { success = true, message = "Ruta insertada satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al insertar ruta");
                return BadRequest(new { success = false, message = "Error al insertar la ruta." });
            }
        }

        [HttpPut("actualizar")]
        public async Task<IActionResult> ActualizarRuta([FromBody] Ruta ruta)
        {
            try
            {
                await _rutaService.ActualizarRuta(
                    ruta.Id,
                    ruta.IdOrigen,
                    ruta.IdDestino,
                    ruta.Distancia.Value
                );

                return Ok(new { success = true, message = "Ruta actualizada satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al actualizar ruta");
                return BadRequest(new { success = false, message = "Error al actualizar la ruta." });
            }
        }

        [HttpGet("leer")]
        public async Task<IActionResult> LeerRutas()
        {
            try
            {
                List<Ruta> rutas = await _rutaService.LeerRutas();
                return Ok(new { success = true, data = rutas });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al leer las rutas");
                return BadRequest(new { success = false, message = "Error al leer las rutas." });
            }
        }
    }
}
