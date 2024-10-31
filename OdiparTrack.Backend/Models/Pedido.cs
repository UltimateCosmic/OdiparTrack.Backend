using System;

namespace OdiparTrack.Models
{
    public class Pedido
    {
        public int Id { get; set; }
        public int IdOrigen { get; set; }
        public Oficina? Origen { get; set; }
        public int IdDestino { get; set; }
        public Oficina? Destino { get; set; }
        public int? Cantidad { get; set; }
        public string? Cliente { get; set; }
        public int IdEnvio { get; set; }
        public Envio? Envio { get; set; }
        public int IdCamion { get; set; }
        public Camion? Camion { get; set; }
    }
}
