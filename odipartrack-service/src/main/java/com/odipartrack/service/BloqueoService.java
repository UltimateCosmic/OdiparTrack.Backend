package com.odipartrack.service;

import com.odipartrack.model.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloqueoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
            bloqueo.setId(rs.getInt("id"));
            bloqueo.setStart(rs.getTimestamp("Fecha_inicio").toLocalDateTime());
            bloqueo.setEnd(rs.getTimestamp("Fecha_fin").toLocalDateTime());
            bloqueo.setIdRoute(rs.getInt("idRuta"));
            return bloqueo;
        });
    }
}
