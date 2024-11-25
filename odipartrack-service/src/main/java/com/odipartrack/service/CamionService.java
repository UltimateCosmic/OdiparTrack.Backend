package com.odipartrack.service;

import com.odipartrack.model.Camion;
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
public class CamionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final SaleService saleService;
    private final RutaPorPedidoService rutaPorPedidoService;
    private final RouteService routeService;

    public CamionService(SaleService saleService, RutaPorPedidoService rutaPorPedidoService, RouteService routeService) {
        this.saleService = saleService;
        this.rutaPorPedidoService = rutaPorPedidoService;
        this.routeService = routeService;
    }

    /**
     * Llama al Stored Procedure LeerTodosCamiones y devuelve todos los camiones.
     *
     * @return Lista de objetos Camion.
     */
    public List<Camion> obtenerCamiones() {
        // Obtener las listas necesarias directamente desde los servicios
        List<Sale> sales = saleService.obtenerPedidos();
        List<RutaPorPedido> rutasPorPedido = rutaPorPedidoService.obtenerRutasPorPedidos();
        List<Route> routes = routeService.obtenerRutas();

        String sql = "CALL LeerTodosCamiones()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            // Asignar datos básicos del camión
            Camion camion = new Camion();
            camion.setId(rs.getInt("id"));
            camion.setCodigo(rs.getString("Codigo"));
            camion.setIdInicio(rs.getString("idOrigen"));
            camion.setCapacidad(rs.getInt("Capacidad"));
            camion.setTiempoNuevoEnvio(rs.getTimestamp("Tiempo_nuevo_envio").toLocalDateTime());
            if (rs.getTimestamp("SalidaMinima") != null) {
                camion.setSalida_minima(rs.getTimestamp("SalidaMinima").toLocalDateTime());
            }

            // Asignar datos de la oficina de origen
            Office inicio = new Office();
            inicio.setId(rs.getInt("idOrigen"));
            inicio.setUbigeo(rs.getString("OrigenUBIGEO"));
            inicio.setCapacity(rs.getInt("OrigenCapacidad"));
            inicio.setLatitude(rs.getDouble("OrigenLatitud"));
            inicio.setLongitude(rs.getDouble("OrigenLongitud"));
            inicio.setRegion(rs.getString("OrigenRegion"));
            inicio.setDepartment(rs.getString("OrigenDepartamento"));
            inicio.setProvince(rs.getString("OrigenProvincia"));
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

            return camion;
        });
    }
}
