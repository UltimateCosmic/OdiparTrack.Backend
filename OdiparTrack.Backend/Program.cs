using Microsoft.EntityFrameworkCore;
using OdiparTrack.Data;
using OdiparTrack.Services;

var builder = WebApplication.CreateBuilder(args);

// Configuraci�n de la conexi�n a la base de datos
var connectionString = builder.Configuration.GetConnectionString("OdiparTrackDB");
builder.Services.AddDbContext<OdiparTrackContext>(options =>
    options.UseSqlServer(connectionString));

// Registrar los servicios en el contenedor de dependencias
builder.Services.AddScoped<EnvioService>();
builder.Services.AddScoped<CamionService>();
builder.Services.AddScoped<OficinaService>();
builder.Services.AddScoped<RutaService>();
builder.Services.AddScoped<PedidoService>();
builder.Services.AddScoped<BloqueoService>();
builder.Services.AddScoped<VelocidadService>();

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

//app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
