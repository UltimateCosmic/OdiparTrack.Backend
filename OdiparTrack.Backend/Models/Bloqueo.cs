using System;

namespace OdiparTrack.Models
{
    public class Bloqueo
    {
        public int Id { get; set; }
        public DateTime FechaInicio { get; set; }
        public DateTime FechaFin { get; set; }
        public int IdRuta { get; set; }
        public Ruta? Ruta { get; set; }
    }
}
