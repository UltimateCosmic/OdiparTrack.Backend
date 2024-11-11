using Microsoft.AspNetCore.Mvc;
using OdiparTrack.Backend.Models;
using OdiparTrack.Services;

namespace OdiparTrack.Controllers
{
    [Route("api/velocidad")]
    [ApiController]
    public class VelocidadController : ControllerBase
    {
        private readonly ILogger<VelocidadController> _logger;
        private readonly VelocidadService _velocidadService;

        public VelocidadController(ILogger<VelocidadController> logger, VelocidadService velocidadService)
        {
            _logger = logger;
            _velocidadService = velocidadService;
        }

        [HttpGet("leer")]
        public async Task<IActionResult> LeerVelocidad()
        {
            try
            {
                List<Velocidad> velocidades = await _velocidadService.LeerVelocidades();
                return Ok(new { success = true, data = velocidades });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al leer las velocidades");
                return BadRequest(new { success = false, message = "Error al leer las velocidades." });
            }
        }
    }
}
