using OdiparTrack.Models;
using System;

namespace OdiparTrack.Backend.Models
{
    public class Velocidad
    {
        public int Id { get; set; }
        public string? RegionOrigen { get; set; }
        public string? RegionDestino { get; set; }
        public int? ValorVelocidad { get; set; }
    }
}
