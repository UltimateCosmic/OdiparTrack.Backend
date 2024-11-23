using MySql.Data.MySqlClient;
using OdiparTrack.Models;
using System.Data;

namespace OdiparTrack.Services
{
    public class OficinaService
    {
        private readonly IConfiguration _configuration;

        public OficinaService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task InsertarOficina(string ubigeo, int capacidad, decimal latitud, decimal longitud, string region, string departamento, string provincia)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("InsertarOficina", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pUBIGEO", ubigeo);
                    cmd.Parameters.AddWithValue("pCapacidad", capacidad);
                    cmd.Parameters.AddWithValue("pLatitud", latitud);
                    cmd.Parameters.AddWithValue("pLongitud", longitud);
                    cmd.Parameters.AddWithValue("pRegion", region);
                    cmd.Parameters.AddWithValue("pDepartamento", departamento);
                    cmd.Parameters.AddWithValue("pProvincia", provincia);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task ActualizarOficina(int idOficina, int? capacidad, decimal? latitud, decimal? longitud, string region, string departamento, string provincia)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("ActualizarOficina", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar parámetros al Stored Procedure
                    cmd.Parameters.AddWithValue("pIdOficina", idOficina);
                    cmd.Parameters.AddWithValue("pCapacidad", capacidad.HasValue ? (object)capacidad.Value : DBNull.Value);
                    cmd.Parameters.AddWithValue("pLatitud", latitud.HasValue ? (object)latitud.Value : DBNull.Value);
                    cmd.Parameters.AddWithValue("pLongitud", longitud.HasValue ? (object)longitud.Value : DBNull.Value);
                    cmd.Parameters.AddWithValue("pRegion", region ?? DBNull.Value.ToString());
                    cmd.Parameters.AddWithValue("pDepartamento", departamento ?? DBNull.Value.ToString());
                    cmd.Parameters.AddWithValue("pProvincia", provincia ?? DBNull.Value.ToString());

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task EliminarOficina(int idOficina)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("EliminarOficina", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    // Agregar el parámetro para el ID de la oficina
                    cmd.Parameters.AddWithValue("pIdOficina", idOficina);

                    await cmd.ExecuteNonQueryAsync();  // Ejecuta el Stored Procedure
                }
            }
        }

        public async Task<List<Oficina>> LeerOficinas()
        {
            var oficinas = new List<Oficina>();

            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("LeerOficina", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        while (await reader.ReadAsync())
                        {
                            var oficina = new Oficina
                            {
                                Id = reader.GetInt32("id"),
                                Ubigeo = reader.GetString("UBIGEO"),
                                Capacidad = reader.IsDBNull("Capacidad") ? (int?)null : reader.GetInt32("Capacidad"),
                                Latitud = reader.IsDBNull("Latitud") ? (decimal?)null : reader.GetDecimal("Latitud"),
                                Longitud = reader.IsDBNull("Longitud") ? (decimal?)null : reader.GetDecimal("Longitud"),
                                Region = reader.IsDBNull("Region") ? null : reader.GetString("Region"),
                                Departamento = reader.IsDBNull("Departamento") ? null : reader.GetString("Departamento"),
                                Provincia = reader.IsDBNull("Provincia") ? null : reader.GetString("Provincia")
                            };

                            oficinas.Add(oficina);
                        }
                    }
                }
            }

            return oficinas;
        }

        public async Task<Oficina> LeerOficinaPorId(int id)
        {
            Oficina oficina = null;

            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("LeerOficinaPorId", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue("oficinaId", id);

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        if (await reader.ReadAsync())
                        {
                            oficina = new Oficina
                            {
                                Id = reader.GetInt32("id"),
                                Ubigeo = reader.GetString("UBIGEO"),
                                Capacidad = reader.IsDBNull("Capacidad") ? (int?)null : reader.GetInt32("Capacidad"),
                                Latitud = reader.IsDBNull("Latitud") ? (decimal?)null : reader.GetDecimal("Latitud"),
                                Longitud = reader.IsDBNull("Longitud") ? (decimal?)null : reader.GetDecimal("Longitud"),
                                Region = reader.IsDBNull("Region") ? null : reader.GetString("Region"),
                                Departamento = reader.IsDBNull("Departamento") ? null : reader.GetString("Departamento"),
                                Provincia = reader.IsDBNull("Provincia") ? null : reader.GetString("Provincia")
                            };
                        }
                    }
                }
            }

            return oficina;
        }
    }
}
