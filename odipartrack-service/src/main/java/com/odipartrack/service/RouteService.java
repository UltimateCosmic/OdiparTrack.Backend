package com.odipartrack.service;

import com.odipartrack.model.Office;
import com.odipartrack.model.Route;
import com.odipartrack.model.Velocidad;

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

            // Asignar datos básicos de la ruta
            route.setId(rs.getInt("id"));
            route.setIdOrigin(rs.getString("idOrigen"));
            route.setIdDestination(rs.getString("idDestino"));
            route.setDistance(rs.getDouble("Distancia"));
            route.setIdVelocity(rs.getInt("idVelocidad"));

            // Asignar datos de la oficina de origen
            Office origin = new Office();
            origin.setId(rs.getInt("origenId"));
            origin.setUbigeo(rs.getString("origenUBIGEO"));
            origin.setCapacity(rs.getInt("origenCapacidad"));
            origin.setLatitude(rs.getDouble("origenLatitud"));
            origin.setLongitude(rs.getDouble("origenLongitud"));
            origin.setRegion(rs.getString("origenRegion"));
            origin.setDepartment(rs.getString("origenDepartamento"));
            origin.setProvince(rs.getString("origenProvincia"));
            route.setOrigin(origin);

            // Asignar datos de la oficina de destino
            Office destination = new Office();
            destination.setId(rs.getInt("destinoId"));
            destination.setUbigeo(rs.getString("destinoUBIGEO"));
            destination.setCapacity(rs.getInt("destinoCapacidad"));
            destination.setLatitude(rs.getDouble("destinoLatitud"));
            destination.setLongitude(rs.getDouble("destinoLongitud"));
            destination.setRegion(rs.getString("destinoRegion"));
            destination.setDepartment(rs.getString("destinoDepartamento"));
            destination.setProvince(rs.getString("destinoProvincia"));
            route.setDestination(destination);

            // Asignar datos de la velocidad
            Velocidad velocity = new Velocidad();
            velocity.setId(rs.getInt("idVelocidad"));
            velocity.setRegion1(rs.getString("region_origen"));
            velocity.setRegion2(rs.getString("region_destino"));
            velocity.setVelocidad(rs.getDouble("velocidad"));
            route.setVelocity(velocity);

            return route;
        });
    }
}
