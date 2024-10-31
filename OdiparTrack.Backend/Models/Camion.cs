using System;

namespace OdiparTrack.Models
{
    public class Camion
    {
        public int Id { get; set; }
        public string? Codigo { get; set; }
        public int IdOrigen { get; set; }
        public Oficina? Origen { get; set; }
        public int? Capacidad { get; set; }
        public DateTime? TiempoNuevoEnvio { get; set; }
    }
}
