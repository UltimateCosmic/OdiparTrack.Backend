using Microsoft.EntityFrameworkCore;
using OdiparTrack.Data;
using OdiparTrack.Services;
using Pomelo.EntityFrameworkCore.MySql.Infrastructure; // Agregar esta línea

var builder = WebApplication.CreateBuilder(args);

// Configuración de la conexión a la base de datos
var connectionString = builder.Configuration.GetConnectionString("OdiparTrackDB");
builder.Services.AddDbContext<OdiparTrackContext>(options =>
    options.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString))); // Cambiado a MySQL

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

builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowFrontend", policy =>
    {
        policy.WithOrigins("http://localhost:5173") // Cambia al puerto de tu frontend
              .AllowAnyMethod()
              .AllowAnyHeader();
    });
});

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

// app.UseHttpsRedirection();

// Habilitar la política de CORS antes de Authorization
app.UseCors("AllowFrontend");

app.UseAuthorization();

app.MapControllers();

app.Run();
