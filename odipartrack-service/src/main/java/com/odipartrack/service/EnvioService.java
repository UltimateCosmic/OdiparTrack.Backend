package com.odipartrack.service;

import com.odipartrack.model.Camion;
import com.odipartrack.model.Envio;
import com.odipartrack.model.Office;
import com.odipartrack.model.Route;
import com.odipartrack.model.RutaPorPedido;
import com.odipartrack.model.Sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
}
