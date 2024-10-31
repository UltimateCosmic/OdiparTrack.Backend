using Microsoft.AspNetCore.Mvc;
using OdiparTrack.Models;
using OdiparTrack.Services;

namespace OdiparTrack.Controllers
{
    [Route("api/pedido")]
    [ApiController]
    public class PedidoController : ControllerBase
    {
        private readonly ILogger<PedidoController> _logger;
        private readonly PedidoService _pedidoService;

        public PedidoController(ILogger<PedidoController> logger, PedidoService pedidoService)
        {
            _logger = logger;
            _pedidoService = pedidoService;
        }

        [HttpPost("crear")]
        public async Task<IActionResult> CrearPedido([FromBody] Pedido pedido)
        {
            try
            {
                await _pedidoService.InsertarPedido(
                    pedido.IdOrigen,
                    pedido.IdDestino,
                    pedido.Cantidad.Value,
                    pedido.Cliente,
                    pedido.IdEnvio
                );

                return Ok(new { success = true, message = "Pedido insertado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al insertar pedido");
                return BadRequest(new { success = false, message = "Error al insertar el pedido." });
            }
        }

        [HttpPut("actualizar")]
        public async Task<IActionResult> ActualizarPedido([FromBody] Pedido pedido)
        {
            try
            {
                await _pedidoService.ActualizarPedido(
                    pedido.Id,
                    pedido.IdOrigen,
                    pedido.IdDestino,
                    pedido.Cantidad.Value,
                    pedido.Cliente,
                    pedido.IdEnvio
                );

                return Ok(new { success = true, message = "Pedido actualizado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al actualizar pedido");
                return BadRequest(new { success = false, message = "Error al actualizar el pedido." });
            }
        }

        [HttpDelete("eliminar/{id}")]
        public async Task<IActionResult> EliminarPedido(int id)
        {
            try
            {
                await _pedidoService.EliminarPedido(id);
                return Ok(new { success = true, message = "Pedido eliminado satisfactoriamente." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al eliminar pedido");
                return BadRequest(new { success = false, message = "Error al eliminar el pedido." });
            }
        }

        [HttpGet("leer")]
        public async Task<IActionResult> LeerPedidos()
        {
            try
            {
                List<Pedido> pedidos = await _pedidoService.LeerPedidos();
                return Ok(new { success = true, data = pedidos });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error al leer los pedidos");
                return BadRequest(new { success = false, message = "Error al leer los pedidos." });
            }
        }
    }
}
