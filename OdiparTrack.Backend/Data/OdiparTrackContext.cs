using Microsoft.EntityFrameworkCore;
using OdiparTrack.Models;

namespace OdiparTrack.Data
{
    public class OdiparTrackContext : DbContext
    {
        public OdiparTrackContext(DbContextOptions<OdiparTrackContext> options) : base(options) { }

        // Define DbSet para cada entidad (tabla) en la base de datos
        public DbSet<Envio> Envios { get; set; }
        public DbSet<Camion> Camiones { get; set; }
        public DbSet<Ruta> Rutas { get; set; }
        public DbSet<Oficina> Oficinas { get; set; }
        public DbSet<Pedido> Pedido { get; set; }
        public DbSet<Bloqueo> Bloqueos { get; set; }
    }
}
