using MySql.Data.MySqlClient;
using OdiparTrack.Backend.Models;
using OdiparTrack.Models;
using System.Data;

namespace OdiparTrack.Services
{
    public class VelocidadService
    {
        private readonly IConfiguration _configuration;

        public VelocidadService(IConfiguration configuration)
        {
            _configuration = configuration;
        }
                
        public async Task<List<Velocidad>> LeerVelocidades()
        {
            var velocidades = new List<Velocidad>();

            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("LeerVelocidades", conn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        while (await reader.ReadAsync())
                        {
                            var velocidad = new Velocidad
                            {
                                Id = reader.GetInt32("id"),
                                RegionOrigen = reader.IsDBNull("region_origen") ? (string?)null : reader.GetString("region_origen"),
                                RegionDestino = reader.IsDBNull("region_destino") ? (string?)null : reader.GetString("region_destino"),
                                ValorVelocidad = reader.IsDBNull("velocidad") ? (int?)null : reader.GetInt32("velocidad")
                            };

                            velocidades.Add(velocidad);
                        }
                    }
                }
            }

            return velocidades;
        }
    }
}
