using MySql.Data.MySqlClient;
using OdiparTrack.Models;
using System.Data;

namespace OdiparTrack.Services
{
    public class BloqueoService
    {
        private readonly IConfiguration _configuration;

        public BloqueoService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task InsertarBloqueo(DateTime fechaInicio, DateTime? fechaFin, int idRuta)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("InsertarBloqueo", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pFecha_inicio", fechaInicio);
                    cmd.Parameters.AddWithValue("pFecha_fin", fechaFin.HasValue ? (object)fechaFin.Value : DBNull.Value);
                    cmd.Parameters.AddWithValue("pIdRuta", idRuta);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task ActualizarBloqueo(int idBloqueo, DateTime fechaInicio, DateTime? fechaFin, int idRuta)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("ActualizarBloqueo", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pIdBloqueo", idBloqueo);
                    cmd.Parameters.AddWithValue("pFecha_inicio", fechaInicio);
                    cmd.Parameters.AddWithValue("pFecha_fin", fechaFin.HasValue ? (object)fechaFin.Value : DBNull.Value);
                    cmd.Parameters.AddWithValue("pIdRuta", idRuta);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task EliminarBloqueo(int idBloqueo)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("EliminarBloqueo", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar el parámetro para el ID del bloqueo
                    cmd.Parameters.AddWithValue("pIdBloqueo", idBloqueo);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task<List<Bloqueo>> LeerBloqueos()
        {
            var bloqueos = new List<Bloqueo>();

            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("LeerBloqueo", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        while (await reader.ReadAsync())
                        {
                            var bloqueo = new Bloqueo
                            {
                                Id = reader.GetInt32("id"),
                                FechaInicio = reader.GetDateTime("Fecha_inicio"),
                                FechaFin = reader.GetDateTime("Fecha_fin"),
                                IdRuta = reader.GetInt32("idRuta")
                            };

                            bloqueos.Add(bloqueo);
                        }
                    }
                }
            }

            return bloqueos;
        }
    }
}
