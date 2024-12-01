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

        public async Task<List<Bloqueo>> LeerBloqueosPorFechaAsync(DateTime startDate)
        {
            var bloqueos = new List<Bloqueo>();

            using (var conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();

                using (var cmd = new MySqlCommand("LeerBloqueosPorFecha", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue("@start_datetime", startDate);

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        while (await reader.ReadAsync())
                        {
                            var bloqueo = new Bloqueo
                            {
                                Id = reader.GetInt32("id"),
                                FechaInicio = reader.GetDateTime("Fecha_inicio"),
                                FechaFin = reader.GetDateTime("Fecha_fin"),
                                IdRuta = reader.GetInt32("idRuta"),
                                Ruta = new Ruta
                                {
                                    Id = reader.GetInt32("idRuta"),
                                    IdOrigen = reader.GetString("rutaOrigenUbigeo"),
                                    Origen = new Oficina
                                    {
                                        Id = reader.GetInt32("origenId"),
                                        Ubigeo = reader.GetString("rutaOrigenUbigeo"),
                                        Capacidad = reader.IsDBNull("origenCapacidad") ? null : reader.GetInt32("origenCapacidad"),
                                        Latitud = reader.IsDBNull("origenLatitud") ? null : reader.GetDecimal("origenLatitud"),
                                        Longitud = reader.IsDBNull("origenLongitud") ? null : reader.GetDecimal("origenLongitud"),
                                        Region = reader.GetString("origenRegion"),
                                        Departamento = reader.GetString("origenDepartamento"),
                                        Provincia = reader.GetString("origenProvincia")
                                    },
                                    IdDestino = reader.GetString("rutaDestinoUbigeo"),
                                    Destino = new Oficina
                                    {
                                        Id = reader.GetInt32("destinoId"),
                                        Ubigeo = reader.GetString("rutaDestinoUbigeo"),
                                        Capacidad = reader.IsDBNull("destinoCapacidad") ? null : reader.GetInt32("destinoCapacidad"),
                                        Latitud = reader.IsDBNull("destinoLatitud") ? null : reader.GetDecimal("destinoLatitud"),
                                        Longitud = reader.IsDBNull("destinoLongitud") ? null : reader.GetDecimal("destinoLongitud"),
                                        Region = reader.GetString("destinoRegion"),
                                        Departamento = reader.GetString("destinoDepartamento"),
                                        Provincia = reader.GetString("destinoProvincia")
                                    },
                                    Distancia = reader.IsDBNull("Distancia") ? null : reader.GetDecimal("Distancia"),
                                    IdVelocidad = reader.IsDBNull("idVelocidad") ? null : reader.GetInt32("idVelocidad")
                                }
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
