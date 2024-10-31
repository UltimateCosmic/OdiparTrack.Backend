using MySql.Data.MySqlClient;
using OdiparTrack.Models;
using System.Data;

namespace OdiparTrack.Services
{
    public class EnvioService
    {
        private readonly IConfiguration _configuration;

        public EnvioService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task InsertarEnvio(DateTime tiempoSalida, DateTime tiempoLlegada, int capacidadRestante, int idCamion)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("InsertarEnvio", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pTiempo_salida", tiempoSalida);
                    cmd.Parameters.AddWithValue("pTiempo_llegada", tiempoLlegada);
                    cmd.Parameters.AddWithValue("pCapacidad_restante", capacidadRestante);
                    cmd.Parameters.AddWithValue("pIdCamion", idCamion);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task ActualizarEnvio(int idEnvio, DateTime tiempoSalida, DateTime? tiempoLlegada, int? capacidadRestante, int idCamion)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("ActualizarEnvio", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pIdEnvio", idEnvio);
                    cmd.Parameters.AddWithValue("pTiempo_salida", tiempoSalida);
                    cmd.Parameters.AddWithValue("pTiempo_llegada", tiempoLlegada.HasValue ? (object)tiempoLlegada.Value : DBNull.Value);
                    cmd.Parameters.AddWithValue("pCapacidad_restante", capacidadRestante.HasValue ? (object)capacidadRestante.Value : DBNull.Value);
                    cmd.Parameters.AddWithValue("pIdCamion", idCamion);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task EliminarEnvio(int idEnvio)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("EliminarEnvio", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar el parámetro para el ID del envío
                    cmd.Parameters.AddWithValue("pIdEnvio", idEnvio);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task<List<Envio>> LeerEnvios()
        {
            var envios = new List<Envio>();

            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("LeerEnvio", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        while (await reader.ReadAsync())
                        {
                            var envio = new Envio
                            {
                                Id = reader.GetInt32("id"),
                                TiempoSalida = reader.GetDateTime("Tiempo_salida"),
                                TiempoLlegada = reader.IsDBNull("Tiempo_llegada") ? (DateTime?)null : reader.GetDateTime("Tiempo_llegada"),
                                CapacidadRestante = reader.IsDBNull("Capacidad_restante") ? (int?)null : reader.GetInt32("Capacidad_restante"),
                                IdCamion = reader.GetInt32("idCamion")
                            };

                            envios.Add(envio);
                        }
                    }
                }
            }

            return envios;
        }
    }
}
