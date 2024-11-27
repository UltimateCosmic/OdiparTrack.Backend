package com.odipartrack.service;

import com.odipartrack.model.Block;
import com.odipartrack.model.Office;
import com.odipartrack.model.Route;
import com.odipartrack.model.Velocidad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BloqueoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("deprecation")
    public List<Block> obtenerBloqueosPorFecha(LocalDateTime fechaHoraInicio) {
        String sql = "CALL LeerBloqueosPorFecha(?)";

        // Mapeo de resultados de la BD a la clase Bloqueo
        return jdbcTemplate.query(sql, new Object[] { Timestamp.valueOf(fechaHoraInicio) }, (rs, rowNum) -> {
            
            Block bloqueo = new Block();

            // Asignar datos básicos del bloqueo
            bloqueo.setId(rs.getInt("id"));
            bloqueo.setStart(rs.getTimestamp("Fecha_inicio").toLocalDateTime());
            bloqueo.setEnd(rs.getTimestamp("Fecha_fin").toLocalDateTime());
            bloqueo.setIdRoute(rs.getInt("idRuta"));

            // Asignar datos de la ruta
            Route route = new Route();
            route.setId(rs.getInt("idRuta"));
            route.setIdOrigin(rs.getString("rutaOrigenUbigeo"));
            route.setIdDestination(rs.getString("rutaDestinoUbigeo"));
            route.setDistance(rs.getDouble("Distancia"));
            route.setIdVelocity(rs.getInt("idVelocidad"));
            bloqueo.setRoute(route);

            // Asignar datos de la oficina de origen
            Office origin = new Office();            
            origin.setUbigeo(rs.getString("rutaOrigenUbigeo"));
            origin.setId(rs.getInt("origenId"));
            origin.setCapacity(rs.getInt("origenCapacidad"));
            origin.setLatitude(rs.getDouble("origenLatitud"));
            origin.setLongitude(rs.getDouble("origenLongitud"));
            origin.setRegion(rs.getString("origenRegion"));
            origin.setDepartment(rs.getString("origenDepartamento"));
            origin.setProvince(rs.getString("origenProvincia"));
            route.setOrigin(origin);

            // Asignar datos de la oficina de destino
            Office destination = new Office();
            destination.setUbigeo(rs.getString("rutaDestinoUbigeo"));
            destination.setId(rs.getInt("destinoId"));            
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

            return bloqueo;
        });
    }
    
    /**
     * Llama al Stored Procedure LeerBloqueo y devuelve todos los bloqueos.
     *
     * @return Lista de objetos Bloqueo.
     */
    public List<Block> obtenerBloqueos() {
        String sql = "CALL LeerBloqueo()";

        // Mapeo de resultados de la BD a la clase Bloqueo
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Block bloqueo = new Block();

            // Asignar datos básicos del bloqueo
            bloqueo.setId(rs.getInt("id"));
            bloqueo.setStart(rs.getTimestamp("Fecha_inicio").toLocalDateTime());
            bloqueo.setEnd(rs.getTimestamp("Fecha_fin").toLocalDateTime());
            bloqueo.setIdRoute(rs.getInt("idRuta"));

            // Asignar datos de la ruta
            Route route = new Route();
            route.setId(rs.getInt("idRuta"));
            route.setIdOrigin(rs.getString("rutaOrigenUbigeo"));
            route.setIdDestination(rs.getString("rutaDestinoUbigeo"));
            route.setDistance(rs.getDouble("Distancia"));
            route.setIdVelocity(rs.getInt("idVelocidad"));
            bloqueo.setRoute(route);

            // Asignar datos de la oficina de origen
            Office origin = new Office();            
            origin.setUbigeo(rs.getString("rutaOrigenUbigeo"));
            origin.setId(rs.getInt("origenId"));
            origin.setCapacity(rs.getInt("origenCapacidad"));
            origin.setLatitude(rs.getDouble("origenLatitud"));
            origin.setLongitude(rs.getDouble("origenLongitud"));
            origin.setRegion(rs.getString("origenRegion"));
            origin.setDepartment(rs.getString("origenDepartamento"));
            origin.setProvince(rs.getString("origenProvincia"));
            route.setOrigin(origin);

            // Asignar datos de la oficina de destino
            Office destination = new Office();
            destination.setUbigeo(rs.getString("rutaDestinoUbigeo"));
            destination.setId(rs.getInt("destinoId"));            
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

            return bloqueo;
        });
    }
}
