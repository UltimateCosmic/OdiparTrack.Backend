using MySql.Data.MySqlClient;
using OdiparTrack.Models;
using System.Data;

namespace OdiparTrack.Services
{
    public class ReporteService
    {
        private readonly IConfiguration _configuration;

        public ReporteService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task InsertarReporte(
            DateTime fechaHoraInicioPlanificacion,
            DateTime fechaHoraFinPlanificacion,
            int cantidadPedidosCompletados,
            int cantidadPedidosPendientes,
            int cantidadVehiculosUtilizados,
            byte[] archivoPdf)
        {
            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("InsertarReporte", conn))
                {
                    cmd.CommandType = CommandType.StoredProcedure;

                    cmd.Parameters.AddWithValue("pFechaHoraInicioPlanificacion", fechaHoraInicioPlanificacion);
                    cmd.Parameters.AddWithValue("pFechaHoraFinPlanificacion", fechaHoraFinPlanificacion);
                    cmd.Parameters.AddWithValue("pCantidadPedidosCompletados", cantidadPedidosCompletados);
                    cmd.Parameters.AddWithValue("pCantidadPedidosPendientes", cantidadPedidosPendientes);
                    cmd.Parameters.AddWithValue("pCantidadVehiculosUtilizados", cantidadVehiculosUtilizados);
                    cmd.Parameters.AddWithValue("pArchivoPdf", archivoPdf);

                    await cmd.ExecuteNonQueryAsync();
                }
            }
        }

        public async Task<List<Reporte>> LeerReportes()
        {
            var reportes = new List<Reporte>();

            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("LeerReportes", conn))
                {
                    cmd.CommandType = CommandType.StoredProcedure;

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        while (await reader.ReadAsync())
                        {
                            var reporte = new Reporte
                            {
                                IdReporte = reader.GetInt32("IdReporte"),
                                FechaHoraInicioPlanificacion = reader.GetDateTime("FechaHoraInicioPlanificacion"),
                                FechaHoraFinPlanificacion = reader.GetDateTime("FechaHoraFinPlanificacion"),
                                CantidadPedidosCompletados = reader.GetInt32("CantidadPedidosCompletados"),
                                CantidadPedidosPendientes = reader.GetInt32("CantidadPedidosPendientes"),
                                CantidadVehiculosUtilizados = reader.GetInt32("CantidadVehiculosUtilizados"),
                                ArchivoPdf = (byte[])reader["ArchivoPdf"]
                            };

                            reportes.Add(reporte);
                        }
                    }
                }
            }

            return reportes;
        }

        public async Task<Reporte> ObtenerReportePorId(int id)
        {
            Reporte reporte = null;

            using (MySqlConnection conn = new MySqlConnection(_configuration.GetConnectionString("OdiparTrackDB")))
            {
                await conn.OpenAsync();
                using (MySqlCommand cmd = new MySqlCommand("ObtenerReportePorId", conn))
                {
                    cmd.CommandType = CommandType.StoredProcedure;
                    cmd.Parameters.AddWithValue("pIdReporte", id);

                    using (var reader = await cmd.ExecuteReaderAsync())
                    {
                        if (await reader.ReadAsync())
                        {
                            reporte = new Reporte
                            {
                                IdReporte = reader.GetInt32("IdReporte"),
                                FechaHoraInicioPlanificacion = reader.GetDateTime("FechaHoraInicioPlanificacion"),
                                FechaHoraFinPlanificacion = reader.GetDateTime("FechaHoraFinPlanificacion"),
                                CantidadPedidosCompletados = reader.GetInt32("CantidadPedidosCompletados"),
                                CantidadPedidosPendientes = reader.GetInt32("CantidadPedidosPendientes"),
                                CantidadVehiculosUtilizados = reader.GetInt32("CantidadVehiculosUtilizados"),
                                ArchivoPdf = (byte[])reader["ArchivoPdf"]
                            };
                        }
                    }
                }
            }

            return reporte;
        }
    }
}
