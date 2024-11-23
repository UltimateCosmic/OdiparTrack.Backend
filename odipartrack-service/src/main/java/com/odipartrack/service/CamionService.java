package com.odipartrack.service;

import com.odipartrack.model.Camion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CamionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al Stored Procedure LeerTodosCamiones y devuelve todos los camiones.
     *
     * @return Lista de objetos Camion.
     */
    public List<Camion> obtenerCamiones() {
        String sql = "CALL LeerTodosCamiones()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Camion camion = new Camion();
            camion.setId(rs.getInt("id"));
            camion.setCodigo(rs.getString("Codigo"));
            camion.setIdInicio(rs.getString("idOrigen"));
            camion.setCapacidad(rs.getInt("Capacidad"));
            camion.setTiempoNuevoEnvio(rs.getTimestamp("Tiempo_nuevo_envio").toLocalDateTime());
            return camion;
        });
    }
}
