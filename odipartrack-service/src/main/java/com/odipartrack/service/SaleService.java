package com.odipartrack.service;

import com.odipartrack.model.Office;
import com.odipartrack.model.Sale;
import com.odipartrack.model.Envio;
import com.odipartrack.model.Camion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final String ARCHIVOS_PATH = "c.1inf54.24-2.ventas.historico.proyectado.v2.0.20241205";
    private final OfficeService officeService;

    public SaleService(OfficeService officeService, JdbcTemplate jdbcTemplate) {
        this.officeService = officeService;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Llama al Stored Procedure LeerPedido y devuelve todos los pedidos.
     *
     * @return Lista de objetos Pedido.
     */
    public List<Sale> obtenerPedidos() {
        String sql = "CALL LeerPedidos()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            // Asignar datos básicos del pedido
            Sale sale = new Sale();
            sale.setId(rs.getInt("id"));
            sale.setIdOrigin(rs.getInt("idOrigen"));
            sale.setIdDestination(rs.getInt("idDestino"));
            sale.setQuantity(rs.getInt("Cantidad"));
            sale.setClientId(rs.getString("Cliente"));
            sale.setIdEnvio(rs.getInt("idEnvio"));
            sale.setIdCamion(rs.getInt("idCamion"));
            sale.setDateTime(getLocalDateTime(rs, "Fecha"));

            // Asignar datos de la oficina de origen
            Office origin = new Office();
            origin.setId(rs.getInt("idOrigen"));
            origin.setUbigeo(rs.getString("OrigenUBIGEO"));
            origin.setCapacity(rs.getInt("OrigenCapacidad"));
            origin.setLatitude(rs.getDouble("OrigenLatitud"));
            origin.setLongitude(rs.getDouble("OrigenLongitud"));
            origin.setRegion(rs.getString("OrigenRegion"));
            origin.setDepartment(rs.getString("OrigenDepartamento"));
            origin.setProvince(rs.getString("OrigenProvincia"));
            sale.setOrigin(origin);

            // Asignar datos de la oficina de destino
            Office destination = new Office();
            destination.setId(rs.getInt("idDestino"));
            destination.setUbigeo(rs.getString("DestinoUbigeo"));
            destination.setCapacity(rs.getInt("DestinoCapacidad"));
            destination.setLatitude(rs.getDouble("DestinoLatitud"));
            destination.setLongitude(rs.getDouble("DestinoLongitud"));
            destination.setRegion(rs.getString("DestinoRegion"));
            destination.setDepartment(rs.getString("DestinoDepartamento"));
            destination.setProvince(rs.getString("DestinoProvincia"));
            sale.setDestination(destination);

            // Asignar datos del camión
            Camion camion = new Camion();
            camion.setId(rs.getInt("idCamion"));
            camion.setCodigo(rs.getString("CamionCodigo"));
            camion.setCapacidad(rs.getInt("CamionCapacidad"));
            camion.setIdInicio(rs.getString("OficinaCamionUBIGEO"));

            // Asignar datos de la oficina de origen del camión
            Office oficinaCamion = new Office();
            oficinaCamion.setId(rs.getInt("idOficinaCamion"));
            oficinaCamion.setUbigeo(rs.getString("OficinaCamionUbigeo"));
            oficinaCamion.setCapacity(rs.getInt("OficinaCamionCapacidad"));
            oficinaCamion.setLatitude(rs.getDouble("OficinaCamionLatitud"));
            oficinaCamion.setLongitude(rs.getDouble("OficinaCamionLongitud"));
            oficinaCamion.setRegion(rs.getString("OficinaCamionRegion"));
            oficinaCamion.setDepartment(rs.getString("OficinaCamionDepartamento"));
            oficinaCamion.setProvince(rs.getString("OficinaCamionProvincia"));
            camion.setInicio(oficinaCamion);

            // Asignar datos del envío
            Envio envio = new Envio();
            envio.setId(rs.getInt("idEnvio"));
            envio.setTiempoLlegada(getLocalDateTime(rs, "TiempoLlegada"));
            envio.setTiempoSalida(getLocalDateTime(rs, "TiempoSalida"));
            envio.setCapacidadRestante(rs.getInt("EnvioCapacidadRestante"));
            envio.setIdCamion(rs.getInt("idCamion"));
            envio.setCamion(camion);
            sale.setEnvio(envio);

            return sale;
        });
    }

    /**
     * Llama al Stored Procedure LeerPedidos y devuelve todos los pedidos.
     *
     * @return Lista de objetos Sale.
     */
    @SuppressWarnings("deprecation")
    public List<Sale> obtenerPedidosPorFecha(LocalDateTime fechaHoraInicio) {
        String sql = "CALL LeerPedidosPorFecha(?)";

        return jdbcTemplate.query(sql, new Object[] { Timestamp.valueOf(fechaHoraInicio) }, (rs, rowNum) -> {

            // Asignar datos básicos del pedido
            Sale sale = new Sale();
            sale.setId(rs.getInt("id"));
            sale.setIdOrigin(rs.getInt("idOrigen"));
            sale.setIdDestination(rs.getInt("idDestino"));
            sale.setQuantity(rs.getInt("Cantidad"));
            sale.setClientId(rs.getString("Cliente"));
            sale.setIdEnvio(rs.getInt("idEnvio"));
            sale.setIdCamion(rs.getInt("idCamion"));
            sale.setDateTime(getLocalDateTime(rs, "Fecha"));

            // Asignar datos de la oficina de origen
            Office origin = new Office();
            origin.setId(rs.getInt("idOrigen"));
            origin.setUbigeo(rs.getString("OrigenUBIGEO"));
            origin.setCapacity(rs.getInt("OrigenCapacidad"));
            origin.setLatitude(rs.getDouble("OrigenLatitud"));
            origin.setLongitude(rs.getDouble("OrigenLongitud"));
            origin.setRegion(rs.getString("OrigenRegion"));
            origin.setDepartment(rs.getString("OrigenDepartamento"));
            origin.setProvince(rs.getString("OrigenProvincia"));
            sale.setOrigin(origin);

            // Asignar datos de la oficina de destino
            Office destination = new Office();
            destination.setId(rs.getInt("idDestino"));
            destination.setUbigeo(rs.getString("DestinoUbigeo"));
            destination.setCapacity(rs.getInt("DestinoCapacidad"));
            destination.setLatitude(rs.getDouble("DestinoLatitud"));
            destination.setLongitude(rs.getDouble("DestinoLongitud"));
            destination.setRegion(rs.getString("DestinoRegion"));
            destination.setDepartment(rs.getString("DestinoDepartamento"));
            destination.setProvince(rs.getString("DestinoProvincia"));
            sale.setDestination(destination);

            // Asignar datos del camión
            Camion camion = new Camion();
            camion.setId(rs.getInt("idCamion"));
            camion.setCodigo(rs.getString("CamionCodigo"));
            camion.setCapacidad(rs.getInt("CamionCapacidad"));
            camion.setTiempoNuevoEnvio(getLocalDateTime(rs, "TiempoNuevoEnvio"));
            camion.setIdInicio(rs.getString("OficinaCamionUBIGEO"));
            if (rs.getTimestamp("SalidaMinima") != null) {
                camion.setSalida_minima(rs.getTimestamp("SalidaMinima").toLocalDateTime());
            }

            // Asignar datos de la oficina de origen del camión
            Office oficinaCamion = new Office();
            oficinaCamion.setId(rs.getInt("idOficinaCamion"));
            oficinaCamion.setUbigeo(rs.getString("OficinaCamionUbigeo"));
            oficinaCamion.setCapacity(rs.getInt("OficinaCamionCapacidad"));
            oficinaCamion.setLatitude(rs.getDouble("OficinaCamionLatitud"));
            oficinaCamion.setLongitude(rs.getDouble("OficinaCamionLongitud"));
            oficinaCamion.setRegion(rs.getString("OficinaCamionRegion"));
            oficinaCamion.setDepartment(rs.getString("OficinaCamionDepartamento"));
            oficinaCamion.setProvince(rs.getString("OficinaCamionProvincia"));
            camion.setInicio(oficinaCamion);

            // Asignar datos del envío
            Envio envio = new Envio();
            envio.setId(rs.getInt("idEnvio"));
            envio.setTiempoLlegada(getLocalDateTime(rs, "TiempoLlegada"));
            envio.setTiempoSalida(getLocalDateTime(rs, "TiempoSalida"));
            envio.setCapacidadRestante(rs.getInt("EnvioCapacidadRestante"));
            envio.setIdCamion(rs.getInt("idCamion"));
            envio.setCamion(camion);
            sale.setEnvio(envio);

            return sale;
        });
    }

    /**
     * Método auxiliar para obtener un LocalDateTime de un ResultSet, manejando
     * valores nulos.
     *
     * @param rs         El ResultSet.
     * @param columnName El nombre de la columna.
     * @return LocalDateTime si no es nulo, de lo contrario, null.
     * @throws SQLException Si ocurre un error al leer el ResultSet.
     */
    private LocalDateTime getLocalDateTime(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return (timestamp != null) ? timestamp.toLocalDateTime() : null;
    }

    /**
     * Procesa todos los archivos de pedidos en la carpeta especificada.
     *
     * @return Total de registros insertados.
     */
    public int procesarArchivosPedidos() throws IOException {

        List<Office> oficinas = officeService.obtenerOficinas();

        File carpeta = new File(ARCHIVOS_PATH);
        File[] archivos = carpeta.listFiles((dir, name) -> name.startsWith("c.1inf54.ventas"));

        if (archivos == null || archivos.length == 0) {
            throw new IOException("No se encontraron archivos en la carpeta especificada.");
        }

        int totalRegistros = 0;

        for (File archivo : archivos) {
            List<String> lineas = Files.readAllLines(archivo.toPath());
            int registrosInsertados = procesarArchivo(archivo.getName(), lineas, oficinas);
            totalRegistros += registrosInsertados;
        }

        return totalRegistros;
    }

    /**
     * Procesa un archivo específico y guarda sus datos en la base de datos.
     *
     * @param nombreArchivo Nombre del archivo (para extraer mes/año).
     * @param lineas        Líneas del archivo.
     * @return Registros insertados.
     */
    private int procesarArchivo(String nombreArchivo, List<String> lineas, List<Office> oficinas) {
        int totalInsertados = 0;

        String anioMes = nombreArchivo.substring(15, 21); // Extraer "202406"
        int anio = Integer.parseInt(anioMes.substring(0, 4));
        int mes = Integer.parseInt(anioMes.substring(4, 6));

        for (String linea : lineas) {
            if (linea.trim().isEmpty())
                continue;
            try {
                Sale pedido = parsearLinea(linea, anio, mes, oficinas);

                System.out.println("===================================================");
                System.out.println("ID Origen: " + pedido.getIdOrigin());
                System.out.println("ID Destino: " + pedido.getIdDestination());
                System.out.println("Cantidad: " + pedido.getQuantity());
                System.out.println("Fecha y Hora: " + pedido.getDateTime());
                System.out.println("===================================================");

                guardarPedidoEnBD(pedido);
                totalInsertados++;
            } catch (Exception e) {
                // Log de errores para la línea específica (opcional)
                System.err.println("Error procesando línea: " + linea + " - " + e.getMessage());
            }
        }

        return totalInsertados;
    }

    /**
     * Convierte una línea del archivo en un objeto Sale.
     *
     * @param linea Línea del archivo.
     * @param anio  Año del archivo.
     * @param mes   Mes del archivo.
     * @return Objeto Sale.
     */
    private Sale parsearLinea(String linea, int anio, int mes, List<Office> oficinas) {
        String[] partes = linea.split(",");
        String[] fechaHora = partes[0].split(" ");
        String[] origenDestino = partes[1].split("=>");
        String cantidad = partes[2].trim();

        int dia = Integer.parseInt(fechaHora[0]);
        LocalDateTime fechaRegistro = LocalDateTime.of(anio, mes, dia,
                Integer.parseInt(fechaHora[1].split(":")[0]), // Hora
                Integer.parseInt(fechaHora[1].split(":")[1]) // Minuto
        );

        Sale sale = new Sale();
        sale.setDateTime(fechaRegistro);

        for (Office oficina : oficinas) {
            if (oficina.getUbigeo().equals(origenDestino[0].trim())) {
                sale.setIdOrigin(oficina.getId());
                break;
            }
        }
        for (Office oficina : oficinas) {
            if (oficina.getUbigeo().equals(origenDestino[1].trim())) {
                sale.setIdDestination(oficina.getId());
                break;
            }
        }

        sale.setQuantity(Integer.parseInt(cantidad.trim()));
        return sale;
    }

    /**
     * Guarda un pedido en la base de datos.
     *
     * @param sale Pedido a guardar.
     */
    private void guardarPedidoEnBD(Sale sale) {
        String sql = "INSERT INTO Pedidos (idOrigen, idDestino, Cantidad, Cliente, Fecha) " +
                "VALUES (?, ?, ?, ?, ?)";
        if (sale.getIdOrigin() == 0) {
            sql = "INSERT INTO Pedidos (idDestino, Cantidad, Cliente, Fecha) " +
                    "VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    sale.getIdDestination(),
                    sale.getQuantity(),
                    sale.getClientId(),
                    sale.getDateTime()); // Inserta LocalDateTime directamente
        } else {
            jdbcTemplate.update(sql,
                    sale.getIdOrigin(),
                    sale.getIdDestination(),
                    sale.getQuantity(),
                    sale.getClientId(),
                    sale.getDateTime()); // Inserta LocalDateTime directamente
        }
    }

}
