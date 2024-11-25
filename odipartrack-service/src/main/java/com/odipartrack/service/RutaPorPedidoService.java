package com.odipartrack.service;

import com.odipartrack.model.RutaPorPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RutaPorPedidoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al Stored Procedure para obtener todas las relaciones Rutas por Pedidos.
     *
     * @return Lista de objetos RutaPorPedido con idPedido y idRuta.
     */
    public List<RutaPorPedido> obtenerRutasPorPedidos() {
        String sql = "CALL leerRutasXPedidos()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RutaPorPedido rutaPorPedido = new RutaPorPedido();
            rutaPorPedido.setIdPedido(rs.getInt("idPedido"));
            rutaPorPedido.setIdRuta(rs.getInt("idRuta"));
            return rutaPorPedido;
        });
    }
}
