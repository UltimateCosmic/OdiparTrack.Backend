package com.odipartrack.service;

import com.odipartrack.model.Envio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvioService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al Stored Procedure LeerEnvio y devuelve todos los envíos.
     *
     * @return Lista de objetos Envio.
     */
    public List<Envio> obtenerEnvios() {
        String sql = "CALL LeerEnvio()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Envio envio = new Envio();
            envio.setId(rs.getInt("id"));
            envio.setTiempoSalida(rs.getTimestamp("Tiempo_salida").toLocalDateTime());
            envio.setTiempoLlegada(rs.getTimestamp("Tiempo_llegada").toLocalDateTime());
            envio.setCapacidadRestante(rs.getInt("Capacidad_restante"));
            envio.setIdCamion(rs.getInt("idCamion"));
            return envio;
        });
    }
}
