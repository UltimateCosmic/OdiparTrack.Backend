using System;
using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace OdiparTrack.Models
{
    public class Reporte
    {
        public int IdReporte { get; set; }
        public DateTime FechaHoraInicioPlanificacion { get; set; }
        public DateTime FechaHoraFinPlanificacion { get; set; }
        public int CantidadPedidosCompletados { get; set; }
        public int CantidadPedidosPendientes { get; set; }
        public int CantidadVehiculosUtilizados { get; set; }
        public byte[] ArchivoPdf { get; set; }
    }
}
