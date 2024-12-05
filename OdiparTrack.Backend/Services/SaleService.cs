using OdiparTrack.Models;
using System;
using System.Collections.Generic;
using System.Data;
using MySql.Data.MySqlClient;

namespace OdiparTrack.Services
{
    public class SaleService
    {
        private readonly IConfiguration _configuration;

        public SaleService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public List<Sale> ObtenerPedidos()
        {
            var pedidos = new List<Sale>();
            using (var connection = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                connection.Open();
                using (var command = new MySqlCommand("CALL LeerPedidos()", connection))
                using (var reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        pedidos.Add(MapearSale(reader));
                    }
                }
            }
            return pedidos;
        }

        public List<Sale> ObtenerPedidosPorFecha(DateTime fechaHoraInicio)
        {
            var pedidos = new List<Sale>();
            using (var connection = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                connection.Open();
                using (var command = new MySqlCommand("CALL LeerPedidosPorFecha(@FechaHora)", connection))
                {
                    command.Parameters.AddWithValue("@FechaHora", fechaHoraInicio);

                    using (var reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            pedidos.Add(MapearSale(reader));
                        }
                    }
                }
            }
            return pedidos;
        }

        private Sale MapearSale(IDataReader reader)
        {
            var sale = new Sale
            {
                Id = Convert.ToInt32(reader["id"]),
                IdOrigin = Convert.ToInt32(reader["idOrigen"]),
                IdDestination = Convert.ToInt32(reader["idDestino"]),
                Quantity = Convert.ToInt32(reader["Cantidad"]),
                ClientId = reader["Cliente"].ToString(),
                DateTime = reader["Fecha"] == DBNull.Value ? null : (DateTime?)reader["Fecha"]
            };

            // Mapea oficinas de origen y destino, y otros detalles si es necesario
            // Esto depende de cómo esté diseñado tu modelo de datos

            return sale;
        }
    }
}

