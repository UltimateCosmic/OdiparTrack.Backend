using MySql.Data.MySqlClient;
using OdiparTrack.Models;
using System.Data;

namespace OdiparTrack.Services
{
    public class PedidoService
    {
        private readonly IConfiguration _configuration;

        public PedidoService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task InsertarPedido(int idOrigen, int idDestino, int cantidad, string cliente, int idEnvio)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("InsertarPedido", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pIdOrigen", idOrigen);
                    cmd.Parameters.AddWithValue("pIdDestino", idDestino);
                    cmd.Parameters.AddWithValue("pCantidad", cantidad);
                    cmd.Parameters.AddWithValue("pCliente", cliente);
                    cmd.Parameters.AddWithValue("pIdEnvio", idEnvio);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task ActualizarPedido(int idPedido, int idOrigen, int idDestino, int cantidad, string cliente, int idEnvio)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("ActualizarPedido", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pIdPedido", idPedido);
                    cmd.Parameters.AddWithValue("pIdOrigen", idOrigen);
                    cmd.Parameters.AddWithValue("pIdDestino", idDestino);
                    cmd.Parameters.AddWithValue("pCantidad", cantidad);
                    cmd.Parameters.AddWithValue("pCliente", cliente);
                    cmd.Parameters.AddWithValue("pIdEnvio", idEnvio);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task EliminarPedido(int idPedido)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("EliminarPedido", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar el parámetro para el ID del pedido
                    cmd.Parameters.AddWithValue("pIdPedido", idPedido);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task<List<Pedido>> LeerPedidos()
        {
            var pedidos = new List<Pedido>();

            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("LeerPedidos", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        while (await reader.ReadAsync())
                        {
                            var pedido = new Pedido
                            {
                                Id = reader.GetInt32("id"),
                                IdOrigen = reader.GetInt32("idOrigen"),
                                IdDestino = reader.GetInt32("idDestino"),
                                Cantidad = reader.IsDBNull("Cantidad") ? (int?)null : reader.GetInt32("Cantidad"),
                                Cliente = reader.GetString("Cliente"),
                                IdEnvio = reader.GetInt32("idEnvio")
                            };

                            pedidos.Add(pedido);
                        }
                    }
                }
            }

            return pedidos;
        }
    }
}
