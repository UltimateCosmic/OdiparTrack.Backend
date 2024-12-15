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

    public void actualizarRutas(List<Route> routes) {
        for (Route route : routes) {
            actualizarRuta(route);
        }
    }

    /**
     * Llama al procedimiento almacenado `ActualizarRuta` para actualizar una ruta
     * específica.
     *
     * @param route la ruta a actualizar.
     */
    private void actualizarRuta(Route route) {
        jdbcTemplate.update(
                "CALL ActualizarRuta(?, ?, ?, ?, ?)",
                route.getId(),
                route.getIdOrigin(),
                route.getIdDestination(),
                route.getDistance(),
                route.getIdVelocity() == 0 ? null : route.getIdVelocity());
    }

    /**
     * Actualiza las distancias de una lista de rutas en la base de datos.
     *
     * @param routes      Lista de rutas a actualizar.
     * @param velocidades Lista de velocidades disponibles.
     */
    public void actualizarDistanciaRuta(List<Route> routes) {
        for (Route route : routes) {
            Office origin = route.getOrigin();
            Office destination = route.getDestination();

            if (origin != null && destination != null) {
                // Calcular distancia utilizando fórmula de Haversine
                double distance = calcularDistancia(
                        origin.getLatitude(), origin.getLongitude(),
                        destination.getLatitude(), destination.getLongitude());
                route.setDistance(distance);

                // Actualizar distancia en la base de datos
                actualizarDistanciaEnBD(route.getId(), distance);
            }
        }
    }

    /**
     * Actualiza el campo de distancia de una ruta específica en la base de datos.
     *
     * @param routeId  ID de la ruta a actualizar.
     * @param distance Nueva distancia calculada.
     */
    private void actualizarDistanciaEnBD(int routeId, double distance) {
        String sql = "UPDATE Ruta SET Distancia = ? WHERE id = ?";
        jdbcTemplate.update(sql, distance, routeId);
        System.out.println("Se hizo update en la ruta con ID: " + routeId);
    }

    /**
     * Calcula la distancia entre dos puntos geográficos usando la fórmula de
     * Haversine.
     *
     * @param lat1 Latitud del punto 1.
     * @param lon1 Longitud del punto 1.
     * @param lat2 Latitud del punto 2.
     * @param lon2 Longitud del punto 2.
     * @return Distancia en kilómetros.
     */
    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en kilómetros

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Retorna la distancia
    }
}
