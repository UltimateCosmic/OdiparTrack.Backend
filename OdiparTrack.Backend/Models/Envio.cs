using System;

namespace OdiparTrack.Models
{
    public class Envio
    {
        public int Id { get; set; }
        public DateTime? TiempoSalida { get; set; }
        public DateTime? TiempoLlegada { get; set; }
        public int? CapacidadRestante { get; set; }
        public int? IdCamion { get; set; }
        public Camion? Camion { get; set; }
    }
}
