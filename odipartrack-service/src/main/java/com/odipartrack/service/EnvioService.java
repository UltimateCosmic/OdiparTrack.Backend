package com.odipartrack.service;

import com.odipartrack.model.Camion;
import com.odipartrack.model.Envio;
import com.odipartrack.model.Office;
import com.odipartrack.model.Route;
import com.odipartrack.model.RutaPorPedido;
import com.odipartrack.model.Sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnvioService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final SaleService saleService;
    private final RutaPorPedidoService rutaPorPedidoService;
    private final RouteService routeService;

    public EnvioService(SaleService saleService, RutaPorPedidoService rutaPorPedidoService, RouteService routeService,
            JdbcTemplate jdbcTemplate) {
        this.saleService = saleService;
        this.rutaPorPedidoService = rutaPorPedidoService;
        this.routeService = routeService;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Llama al Stored Procedure LeerEnvio y devuelve todos los envíos.
     *
     * @return Lista de objetos Envio.
     */
    public List<Envio> obtenerEnvios() {
        // Obtener las listas necesarias directamente desde los servicios
        List<Sale> sales = saleService.obtenerPedidos();
        List<RutaPorPedido> rutasPorPedido = rutaPorPedidoService.obtenerRutasPorPedidos();
        List<Route> routes = routeService.obtenerRutas();

        String sql = "CALL LeerEnvio()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            // Asignar datos básicos del envio
            Envio envio = new Envio();
            envio.setId(rs.getInt("id"));
            envio.setTiempoSalida(rs.getTimestamp("Tiempo_salida").toLocalDateTime());
            envio.setTiempoLlegada(rs.getTimestamp("Tiempo_llegada").toLocalDateTime());
            envio.setCapacidadRestante(rs.getInt("Capacidad_restante"));
            envio.setIdCamion(rs.getInt("idCamion"));

            // Asignar datos básicos del camión
            Camion camion = new Camion();
            camion.setId(rs.getInt("idCamion"));
            camion.setCodigo(rs.getString("CamionCodigo"));
            camion.setIdInicio(rs.getString("OficinaUBIGEO"));
            camion.setCapacidad(rs.getInt("CamionCapacidad"));
            camion.setTiempoNuevoEnvio(rs.getTimestamp("TiempoNuevoEnvio").toLocalDateTime());
            if (rs.getTimestamp("SalidaMinima") != null) {
                camion.setSalida_minima(rs.getTimestamp("SalidaMinima").toLocalDateTime());
            }

            // Asignar datos de la oficina de origen
            Office inicio = new Office();
            inicio.setId(rs.getInt("OficinaID"));
            inicio.setUbigeo(rs.getString("OficinaUBIGEO"));
            inicio.setCapacity(rs.getInt("OficinaCapacidad"));
            inicio.setLatitude(rs.getDouble("OficinaLatitud"));
            inicio.setLongitude(rs.getDouble("OficinaLongitud"));
            inicio.setRegion(rs.getString("OficinaRegion"));
            inicio.setDepartment(rs.getString("OficinaDepartamento"));
            inicio.setProvince(rs.getString("OficinaProvincia"));
            camion.setInicio(inicio);

            // Filtrar y asignar pedidos (Sale) asociados al camión actual
            List<Sale> pedidosFiltrados = sales.stream()
                    .filter(sale -> sale.getIdCamion() == camion.getId())
                    .collect(Collectors.toList());
            camion.setPedidos(pedidosFiltrados);

            // Filtrar y asignar rutas (Route) asociadas al camión
            List<Route> rutasFiltradas = rutasPorPedido.stream()
                    // Filtrar las rutas que están asociadas a los pedidos del camión actual
                    .filter(rutaPorPedido -> pedidosFiltrados.stream()
                            .anyMatch(sale -> sale.getId() == rutaPorPedido.getIdPedido()))
                    // Mapear las rutas encontradas a objetos Route
                    .map(rutaPorPedido -> routes.stream()
                            .filter(route -> route.getId() == rutaPorPedido.getIdRuta())
                            .findFirst()
                            .orElse(null)) // Manejo de rutas no encontradas
                    .filter(route -> route != null) // Filtrar nulos en caso de rutas no encontradas
                    .collect(Collectors.toList());
            camion.setRutas(rutasFiltradas);

            envio.setCamion(camion);

            return envio;
        });
    }

    /**
     * Inserta envíos y actualiza pedidos relacionados.
     *
     * @param envios Lista de envíos del JSON.
     */
    public void procesarEnvios(List<Envio> envios) {
        for (Envio envio : envios) {
            // Inserta el envío y recupera el ID generado
            int idEnvio = insertarEnvio(envio);

            if (existeCamion(envio.getCamion().getId())) {
                // Actualiza el idEnvio y idCamion en los pedidos relacionados
                for (Sale pedido : envio.getCamion().getPedidos()) {
                    actualizarPedido(pedido.getId(), idEnvio, envio.getCamion().getId());
                }
            }
        }
    }

    /**
     * Inserta un envío en la base de datos utilizando el SP y devuelve el ID
     * generado.
     *
     * @param envio Objeto Envio a insertar.
     * @return ID generado para el envío.
     */
    private int insertarEnvio(Envio envio) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Envio (Tiempo_salida, Tiempo_llegada, Capacidad_restante, idCamion) " +
                    "VALUES (?, ?, ?, (SELECT id FROM Camion WHERE Codigo = ?))", // Subconsulta para obtener el ID
                    Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(envio.getTiempoSalida())); // Tiempo de salida
            ps.setTimestamp(2, Timestamp.valueOf(envio.getTiempoLlegada())); // Tiempo de llegada
            ps.setInt(3, envio.getCapacidadRestante()); // Capacidad restante
            ps.setString(4, envio.getCamion().getCodigo()); // Código del camión
            return ps;
        }, keyHolder);
    
        // Retorna el ID generado para el envío
        return keyHolder.getKey().intValue();
    }
    
    /**
     * Actualiza el idEnvio y idCamion en la tabla pedidos.
     *
     * @param idPedido ID del pedido a actualizar.
     * @param idEnvio  ID del envío relacionado.
     * @param idCamion ID del camión relacionado.
     */
    private void actualizarPedido(int idPedido, int idEnvio, int idCamion) {
        jdbcTemplate.update(
                "UPDATE Pedidos SET idEnvio = ?, idCamion = ? WHERE id = ?",
                idEnvio, idCamion, idPedido);
    }

    public boolean existeCamion(int idCamion) {
        String sql = "SELECT COUNT(*) FROM Camion WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idCamion);
        return count != null && count > 0;
    }

}
