package com.odipartrack.service;

import com.odipartrack.model.Velocidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VelocidadService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al Stored Procedure LeerVelocidades y devuelve todas las velocidades.
     *
     * @return Lista de objetos Velocidad.
     */
    public List<Velocidad> obtenerVelocidades() {
        String sql = "CALL LeerVelocidades()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Velocidad velocidad = new Velocidad();
            velocidad.setId(rs.getInt("id"));
            velocidad.setRegion1(rs.getString("region_origen"));
            velocidad.setRegion2(rs.getString("region_destino"));
            velocidad.setVelocidad(rs.getDouble("velocidad"));
            return velocidad;
        });
    }
}
