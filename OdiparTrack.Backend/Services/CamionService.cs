using MySql.Data.MySqlClient;
using OdiparTrack.Models;
using System.Data;

namespace OdiparTrack.Services
{
    public class CamionService
    {
        private readonly IConfiguration _configuration;

        public CamionService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task InsertarCamion(string codigo, int idOrigen, int capacidad, DateTime tiempoNuevoEnvio)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("InsertarCamion", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pCodigo", codigo);
                    cmd.Parameters.AddWithValue("pIdOrigen", idOrigen);
                    cmd.Parameters.AddWithValue("pCapacidad", capacidad);
                    cmd.Parameters.AddWithValue("pTiempo_nuevo_envio", tiempoNuevoEnvio);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task ActualizarCamion(int idCamion, string codigo, int idOrigen, int capacidad, DateTime? tiempoNuevoEnvio)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("ActualizarCamion", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pIdCamion", idCamion);
                    cmd.Parameters.AddWithValue("pCodigo", codigo);
                    cmd.Parameters.AddWithValue("pIdOrigen", idOrigen);
                    cmd.Parameters.AddWithValue("pCapacidad", capacidad);
                    cmd.Parameters.AddWithValue("pTiempo_nuevo_envio", tiempoNuevoEnvio.HasValue ? (object)tiempoNuevoEnvio.Value : DBNull.Value);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task<List<Camion>> LeerTodosCamiones()
        {
            var camiones = new List<Camion>();

            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("LeerTodosCamiones", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        while (await reader.ReadAsync())
                        {
                            var camion = new Camion
                            {
                                Id = reader.GetInt32("id"),
                                Codigo = reader.GetString("Codigo"),
                                IdOrigen = reader.GetInt32("idOrigen"),
                                Capacidad = reader.IsDBNull("Capacidad") ? (int?)null : reader.GetInt32("Capacidad"),
                                TiempoNuevoEnvio = reader.IsDBNull("Tiempo_nuevo_envio") ? (DateTime?)null : reader.GetDateTime("Tiempo_nuevo_envio")
                            };

                            camiones.Add(camion);
                        }
                    }
                }
            }

            return camiones;
        }
    }
}
