using System;

namespace OdiparTrack.Models
{
    public class Oficina
    {
        public int Id { get; set; }
        public int? Capacidad { get; set; }
        public decimal? Latitud { get; set; }
        public decimal? Longitud { get; set; }
        public string? Region { get; set; }
        public string? Departamento { get; set; }
        public string? Provincia { get; set; }
    }
}
