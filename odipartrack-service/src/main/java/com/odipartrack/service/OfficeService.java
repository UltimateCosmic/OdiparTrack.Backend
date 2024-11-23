package com.odipartrack.service;

import com.odipartrack.model.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al Stored Procedure LeerOficina y devuelve todas las oficinas.
     *
     * @return Lista de objetos Office.
     */
    public List<Office> obtenerOficinas() {
        String sql = "CALL LeerOficina()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Office office = new Office(
                rs.getInt("id"),
                rs.getString("UBIGEO"),
                rs.getString("Departamento"),
                rs.getString("Provincia"),
                rs.getDouble("Latitud"),
                rs.getDouble("Longitud"),
                rs.getString("Region"),
                rs.getInt("Capacidad")
            );
            return office;
        });
    }
}
