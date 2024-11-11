using Microsoft.AspNetCore.Mvc;
using OdiparTrack.Models;
using OdiparTrack.Services;

namespace OdiparTrack.Controllers
{
    [Route("api/oficina")]
    [ApiController]
    public class OficinaController : ControllerBase
    {
        private readonly ILogger<OficinaController> _logger;
        private readonly OficinaService _oficinaService;

        public OficinaController(ILogger<OficinaController> logger, OficinaService oficinaService)
        {
            _logger = logger;
            _oficinaService = oficinaService;
        }

        [HttpPost("crear")]
        public async Task<IActionResult> CrearOficina([FromBody] Oficina oficina)
        {
            try
            {
                await _oficinaService.InsertarOficina(
                    oficina.Ubigeo,
                    oficina.Capacidad.Value,
                    oficina.Latitud.Value,
                    oficina.Longitud.Value,
                    oficina.Region,
                    oficina.Departamento,
                    oficina.Provincia
                );

                return Ok(new { success = true, message = "Oficina insertada satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al insertar oficina");
                return BadRequest(new { success = false, message = "Error al insertar la oficina." });
            }
        }

        [HttpPut("actualizar")]
        public async Task<IActionResult> ActualizarOficina([FromBody] Oficina oficina)
        {
            try
            {
                await _oficinaService.ActualizarOficina(
                    oficina.Id,
                    oficina.Capacidad,
                    oficina.Latitud,
                    oficina.Longitud,
                    oficina.Region,
                    oficina.Departamento,
                    oficina.Provincia
                );

                return Ok(new { success = true, message = "Oficina actualizada satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al actualizar oficina");
                return BadRequest(new { success = false, message = "Error al actualizar la oficina." });
            }
        }

        [HttpDelete("eliminar/{id}")]
        public async Task<IActionResult> EliminarOficina(int id)
        {
            try
            {
                await _oficinaService.EliminarOficina(id);
                return Ok(new { success = true, message = "Oficina eliminada satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al eliminar oficina");
                return BadRequest(new { success = false, message = "Error al eliminar la oficina." });
            }
        }

        [HttpGet("leer")]
        public async Task<IActionResult> LeerOficinas()
        {
            try
            {
                List<Oficina> oficinas = await _oficinaService.LeerOficinas();
                return Ok(new { success = true, data = oficinas });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al leer las oficinas");
                return BadRequest(new { success = false, message = "Error al leer las oficinas." });
            }
        }
    }
}
