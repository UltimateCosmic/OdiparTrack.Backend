using System;

namespace OdiparTrack.Models
{
    public class Ruta
    {
        public int Id { get; set; }
        public int IdOrigen { get; set; }
        public Oficina? Origen { get; set; }
        public int IdDestino { get; set; }
        public Oficina? Destino { get; set; }
        public decimal? Distancia { get; set; }
    }
}
