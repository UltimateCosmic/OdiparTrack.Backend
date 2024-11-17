using System;

namespace OdiparTrack.Models
{
    public class Ruta
    {
        public int Id { get; set; }
        public string IdOrigen { get; set; }
        public Oficina? Origen { get; set; }
        public string IdDestino { get; set; }
        public Oficina? Destino { get; set; }
        public decimal? Distancia { get; set; }
        public int? IdVelocidad { get; set; }
    }
}
