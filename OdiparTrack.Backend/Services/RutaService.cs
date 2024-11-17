using MySql.Data.MySqlClient;
using OdiparTrack.Models;
using System.Data;

namespace OdiparTrack.Services
{
    public class RutaService
    {
        private readonly IConfiguration _configuration;

        public RutaService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task InsertarRuta(string idOrigen, string idDestino, decimal distancia)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("InsertarRuta", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pIdOrigen", idOrigen);
                    cmd.Parameters.AddWithValue("pIdDestino", idDestino);
                    cmd.Parameters.AddWithValue("pDistancia", distancia);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task ActualizarRuta(int idRuta, string idOrigen, string idDestino, decimal distancia, int idVelocidad)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("ActualizarRuta", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pIdRuta", idRuta);
                    cmd.Parameters.AddWithValue("pIdOrigen", idOrigen);
                    cmd.Parameters.AddWithValue("pIdDestino", idDestino);
                    cmd.Parameters.AddWithValue("pDistancia", distancia);
                    cmd.Parameters.AddWithValue("pIdVelocidad", idVelocidad);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task<List<Ruta>> LeerRutas()
        {
            var rutas = new List<Ruta>();

            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("LeerRuta", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        while (await reader.ReadAsync())
                        {
                            var ruta = new Ruta
                            {
                                Id = reader.GetInt32("id"),
                                IdOrigen = reader.GetString("idOrigen"),
                                IdDestino = reader.GetString("idDestino"),
                                Distancia = reader.IsDBNull("Distancia") ? (decimal?)null : reader.GetDecimal("Distancia")
                            };

                            rutas.Add(ruta);
                        }
                    }
                }
            }

            return rutas;
        }
    }
}
