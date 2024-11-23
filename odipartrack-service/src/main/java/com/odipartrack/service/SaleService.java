package com.odipartrack.service;

import com.odipartrack.model.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al Stored Procedure LeerPedidos y devuelve todos los pedidos.
     *
     * @return Lista de objetos Sale.
     */
    public List<Sale> obtenerPedidos() {
        String sql = "CALL LeerPedidos()";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Sale sale = new Sale();
            sale.setId(rs.getInt("id"));
            sale.setIdOrigin(rs.getInt("idOrigen"));
            sale.setIdDestination(rs.getInt("idDestino"));
            sale.setQuantity(rs.getInt("Cantidad"));
            sale.setClientId(rs.getString("Cliente"));
            sale.setIdEnvio(rs.getInt("idEnvio"));
            sale.setIdCamion(rs.getInt("idCamion"));
            sale.setDateTime(rs.getTimestamp("Fecha").toLocalDateTime());
            return sale;
        });
    }
}
