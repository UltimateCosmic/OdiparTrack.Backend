package com.odipartrack.service;

import com.odipartrack.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al Stored Procedure LeerRuta y devuelve todas las rutas.
     *
     * @return Lista de objetos Route.
     */
    public List<Route> obtenerRutas() {
        String sql = "CALL LeerRuta()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Route route = new Route();
            route.setId(rs.getInt("id"));
            route.setIdOrigin(rs.getString("idOrigen"));
            route.setIdDestination(rs.getString("idDestino"));
            route.setDistance(rs.getDouble("Distancia"));
            route.setIdVelocity(rs.getInt("idVelocidad"));
            return route;
        });
    }
}
